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
import com.zhanghao.gankio.model.GankDataLocalRepository;
import com.zhanghao.gankio.model.GankDataRemoteRepository;
import com.zhanghao.gankio.presenter.GankSearchPresenter;
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

public class SearchActivity extends BaseToolbarActivity implements GankContract.SearchLocalView,GankContract.SearchRemoteView{
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
    private GankSearchPresenter mGankSearchPresenter;


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
        mGankSearchPresenter = new GankSearchPresenter(
                this, this,
                GankDataLocalRepository.getInstance(),
                GankDataRemoteRepository.getInstance());
        initView();
        initData();
    }

    private void initData() {
        mGankSearchPresenter.getSearchHistory(this);
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
                mGankSearchPresenter.getSearchResult(SearchActivity.this, query.trim(), false);
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
    public void setUpSearchResult(String word,List<GankSearchItem> datas, boolean isLoadMore) {

        if (!isLoadMore) {
            mGankSearchPresenter.updateSearchHistory(this,word);//加入历史搜索
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
                mGankSearchPresenter.getSearchResult(null, null, true);
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
            mGankSearchPresenter.deleteOneHistory(this, s);
            mHistoryAdapter.notifyDataSetChanged();
            if (mHistoryDatas.isEmpty())
                mHistoryAdapter.removeAllFooterView();
        });
        mHistoryAdapter.getFooterLayout().setOnClickListener(v -> {
            mHistoryDatas.clear();
            mGankSearchPresenter.deleteAllHistory(this);
            mHistoryAdapter.notifyDataSetChanged();
            mHistoryAdapter.removeAllFooterView();
        });
    }
    //数据模拟   因为服务器无法分页返回
    private void mockDatas(int page) {
        int offset = page * 20;
        int totalSize = mDatas.size();
        if (totalSize == 0) {
            showError(Constant.GET_SEARCH_DATA_ERROR);
            return;
        }

        int subSize = totalSize - offset;

        if (subSize >= 20) {
            List<GankSearchItem> subList = mDatas.subList(offset - 20, offset);
            mMockDatas.addAll(subList);
        } else if (subSize > 0 && subSize<20){
            mMockDatas.addAll(mDatas.subList((totalSize - mMockDatas.size()), totalSize));
        }else{
            mResultAdapter.loadMoreEnd();
            return;
        }
        mPage++;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    @Override
    public void setPresenter(Object presenter) {
        // TODO: 2017/6/13 nothing
    }


}
