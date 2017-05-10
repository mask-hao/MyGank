package com.zhanghao.gankio.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhanghao.gankio.R;
import com.zhanghao.gankio.contract.GankContract;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.GankSearchItem;
import com.zhanghao.gankio.model.GankDataRepository;
import com.zhanghao.gankio.presenter.GankPresenter;
import com.zhanghao.gankio.ui.adapter.SearchHistoryAdapter;
import com.zhanghao.gankio.ui.adapter.SearchResultAdapter;
import com.zhanghao.gankio.ui.widget.CustomLoadMore;
import com.zhanghao.gankio.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhanghao on 2017/5/5.
 */

public class SearchActivity extends BaseActivity<GankContract.SearchPresenter> implements GankContract.SearchView {
    private static final String TAG = "SearchActivity";
    @BindView(R.id.gank_search_sv)
    SearchView gankSearchSv;
    @BindView(R.id.gank_search_rl)
    RecyclerView gankSearchRl;
    @BindView(R.id.search_cpb)
    ContentLoadingProgressBar searchCpb;
    @BindView(R.id.search_frame_error_view)
    FrameLayout searchFrameErrorView;
    @BindView(R.id.search_back_iv)
    ImageView searchBackIv;
    private SearchHistoryAdapter mHistoryAdapter;
    private SearchResultAdapter mResultAdapter;
    private List<GankSearchItem> mDatas = new ArrayList<>();
    private List<GankSearchItem> mMockDatas = new ArrayList<>();
    private List<String> mHistoryDatas = new ArrayList<>();

    private int mPage = 1;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected boolean canBack() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        new GankPresenter(this, GankDataRepository.getInstance());
        initView();
        initData();
    }

    private void initData() {
        mPresenter.getSearchHistory(this);
    }

    private void initView() {
        searchBackIv.setOnClickListener(v -> finish());
        gankSearchRl.setLayoutManager(new LinearLayoutManager(this));
        gankSearchSv.onActionViewExpanded();
        gankSearchSv.setQueryHint("请输入关键词");
        gankSearchSv.setIconified(false);
        gankSearchSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchFrameErrorView.getVisibility() == View.VISIBLE)
                    searchFrameErrorView.setVisibility(View.GONE);

                mPage = 1;

                if (mHistoryAdapter != null) {
                    mHistoryDatas.clear();
                    mHistoryAdapter.notifyDataSetChanged();
                    mHistoryAdapter.removeAllFooterView();
                }

                if (mResultAdapter != null) {
                    mDatas.clear();
                    mMockDatas.clear();
                    mResultAdapter.notifyDataSetChanged();
                }
                mPresenter.getSearchResult(SearchActivity.this, query.trim(), false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    @Override
    public void showDialog() {
        searchCpb.setVisibility(View.VISIBLE);
        searchCpb.show();
    }

    @Override
    public void hideDialog() {
        searchCpb.setVisibility(View.GONE);
        searchCpb.hide();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (message.equals(Constant.GET_SEARCH_DATA_ERROR) ||
                message.equals(Constant.NO_MORE_DATA))
            searchFrameErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(GankContract.SearchPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setUpSearchResult(List<GankSearchItem> datas, boolean isLoadMore) {


        if (!isLoadMore) {
            mDatas.addAll(datas);
            mockDatas(mPage);
            mResultAdapter = new SearchResultAdapter(R.layout.gank_search_result_item, mMockDatas);
            gankSearchRl.setAdapter(mResultAdapter);
            mResultAdapter.setOnItemClickListener((adapter, view, position) -> {
                GankSearchItem item = mMockDatas.get(position);
                String url = item.getUrl();
                String title = item.getTitle();
                ActivityUtil.gotoDetailActivity(this, url, title);
            });
            mResultAdapter.setLoadMoreView(new CustomLoadMore());
            mResultAdapter.setOnLoadMoreListener(() -> {
                mPresenter.getSearchResult(null, null, true);
            }, gankSearchRl);
        } else {
            new Handler().postDelayed(() -> {
                mockDatas(mPage);
                mResultAdapter.loadMoreComplete();
            }, 1000);

        }

    }

    @Override
    public void setUpSearchHistory(List<String> histories) {
        if (histories.size() == 0)
            return;
        mHistoryDatas.addAll(histories);

        mHistoryAdapter = new SearchHistoryAdapter(R.layout.gank_search_history_item, mHistoryDatas);

        if (mHistoryDatas.size() >= 1) {
            mHistoryAdapter.addFooterView(LayoutInflater.from(this).inflate(R.layout.gank_search_delete_footer,
                    (ViewGroup) gankSearchRl.getParent(), false));
        }

        gankSearchRl.setAdapter(mHistoryAdapter);

        mHistoryAdapter.setOnItemClickListener((adapter, view, position) -> {
            String query = mHistoryDatas.get(position);
            gankSearchSv.setQuery(query, true);
        });
        mHistoryAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            String s = mHistoryDatas.remove(position);
            mPresenter.deleteOneHistory(this, s);
            mHistoryAdapter.notifyDataSetChanged();
            if (mHistoryDatas.isEmpty())
                mHistoryAdapter.removeAllFooterView();
        });
        mHistoryAdapter.getFooterLayout().setOnClickListener(v -> {
            mHistoryDatas.clear();
            mPresenter.deleteAllHistory(this);
            mHistoryAdapter.notifyDataSetChanged();
            mHistoryAdapter.removeAllFooterView();
        });
    }


    private void mockDatas(int page) {
        int offest = page * 20;
        int totalSize = mDatas.size();
        if (totalSize == 0) {
            showError(Constant.GET_SEARCH_DATA_ERROR);
            return;
        }
        if (totalSize - offest >= 0) {
            List<GankSearchItem> subList = mDatas.subList(page - 1, offest - 1);
            mMockDatas.addAll(subList);
        } else {
            mMockDatas.addAll(mDatas.subList(page - 1, totalSize));
        }
        if (mMockDatas.size() == 0) {
            Toast.makeText(this, "无结果", Toast.LENGTH_SHORT).show();
        }
        mPage++;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
