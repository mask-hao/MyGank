package com.zhanghao.gankio.ui.fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.contract.GankContract;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.GankSection;
import com.zhanghao.gankio.entity.Tag;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.listener.HomeFrgListener;
import com.zhanghao.gankio.listener.RecyclerScrollListener;
import com.zhanghao.gankio.model.GankDataLocalRepository;
import com.zhanghao.gankio.model.GankDataRemoteRepository;
import com.zhanghao.gankio.presenter.GankRecommendPresenter;
import com.zhanghao.gankio.ui.adapter.RecommendAdapter;
import com.zhanghao.gankio.ui.adapter.TagAdapter;
import com.zhanghao.gankio.ui.widget.MyFloatingActionButton;
import com.zhanghao.gankio.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhanghao on 2017/6/22.
 */

public class RecommendFragment extends BaseFragment<GankContract.RecommendPresenter>
        implements GankContract.RecommendView {


    private static final String TAG = "RecommendFragment";
    private static final int START_REFRESH = 100;

    @BindView(R.id.load_failed_tv)
    TextView loadFailedTv;
    @BindView(R.id.load_failed_ll)
    LinearLayout loadFailedLl;
    @BindView(R.id.gankRecommendlist_rl)
    RecyclerView gankRecommendlistRl;
    @BindView(R.id.gankRecommend_srl)
    SwipeRefreshLayout gankRecommendSrl;
    @BindView(R.id.Recommend_search_fab)
    MyFloatingActionButton recommendSearchFab;
    Unbinder unbinder;
    private List<MultiItemEntity> mDatas;
    private RecommendAdapter recommendAdapter;
    private HomeFrgListener homeFrgListener;


    public static RecommendFragment getInstance() {
        RecommendFragment recommendFragment = new RecommendFragment();
        new GankRecommendPresenter(recommendFragment,
                GankDataLocalRepository.getInstance(),
                GankDataRemoteRepository.getInstance());
        return recommendFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.recommend;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = getRoot(inflater, container);
        unbinder = ButterKnife.bind(this, root);
        initView();
        return root;
    }

    private void initView() {

        recommendSearchFab.setOnClickListener(v -> {
            ActivityUtil.gotoSearchActivity(getContext());
        });


        gankRecommendSrl.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);
        gankRecommendSrl.setProgressViewOffset(true, 100, 200);
        gankRecommendSrl.setOnRefreshListener(() -> {
            if (mPresenter != null)
                mPresenter.getCommonRecommend(User.getInstance(), getContext(), true);
        });
        if (mPresenter != null)
            mPresenter.getCommonRecommend(User.getInstance(), getContext(), false);
        gankRecommendlistRl.addOnScrollListener(recyclerScrollListener);
    }


    private RecyclerScrollListener recyclerScrollListener = new RecyclerScrollListener() {
        @Override
        public void hideToolBar() {
            if (homeFrgListener != null)
                homeFrgListener.hideToolbar();
        }

        @Override
        public void showToolBar() {
            if (homeFrgListener != null)
                homeFrgListener.showToolbar();
        }

        @Override
        public void hideBottomBar() {
            if (homeFrgListener != null)
                homeFrgListener.hideBottomBar();
            if (!recommendSearchFab.isHidden())
                recommendSearchFab.hide();
        }

        @Override
        public void showBottomBar() {
            if (homeFrgListener != null)
                homeFrgListener.showBottomBar();
            if (recommendSearchFab.isHidden())
                recommendSearchFab.show();
        }

        @Override
        public void firstVisibleItemPosition(int position) {

        }
    };


    @Override
    public void showDialog() {
        gankRecommendSrl.setEnabled(true);
        if (!gankRecommendSrl.isRefreshing())
            gankRecommendSrl.post(() -> {
                gankRecommendSrl.setRefreshing(true);
            });
    }

    @Override
    public void hideDialog() {
        if (gankRecommendSrl.isRefreshing())
            gankRecommendSrl.post(() -> {
                gankRecommendSrl.setRefreshing(false);
            });
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        if (mDatas==null||mDatas.isEmpty()){
            loadFailedLl.setVisibility(View.VISIBLE);
            loadFailedTv.setOnClickListener(v->{
                mPresenter.getCommonRecommend(User.getInstance(),getContext(),false);
                loadFailedLl.setVisibility(View.GONE);
            });
        }
    }

    @Override
    public void setPresenter(GankContract.RecommendPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setRecommendData(List<MultiItemEntity> customList, boolean refresh) {

        if (recommendSearchFab.getVisibility()==View.GONE)
            recommendSearchFab.setVisibility(View.VISIBLE);
        if (gankRecommendlistRl.getAdapter() instanceof TagAdapter)
            gankRecommendlistRl.removeAllViews();
        if (!refresh) {
            mDatas = new ArrayList<>();
            mDatas.addAll(customList);
            recommendAdapter = new RecommendAdapter(mDatas, getActivity());
            recommendAdapter.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.recycler_header,
                    (ViewGroup) gankRecommendlistRl.getParent(), false));
            recommendAdapter.addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.recycler_click_loadmore,
                    (ViewGroup) gankRecommendlistRl.getParent(), false));
            gankRecommendlistRl.setLayoutManager(new LinearLayoutManager(getContext()));
            gankRecommendlistRl.setAdapter(recommendAdapter);
            recommendAdapter.getFooterLayout().findViewById(R.id.click_load_more_tv)
                    .setOnClickListener(v -> {
                        gankRecommendlistRl.post(() -> {
                            gankRecommendlistRl.smoothScrollToPosition(0);
                            handler.sendEmptyMessage(START_REFRESH);
                        });

                    });
            recommendAdapter.setOnItemClickListener((adapter, view, position) -> {
                onItemClick(position);
            });

        } else {
            mDatas.addAll(0, customList);
            recommendAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
            recommendAdapter.notifyDataSetChanged();
            gankRecommendlistRl.post(() -> {
                gankRecommendlistRl.smoothScrollToPosition(0);
            });
        }

    }

    private void onItemClick(int position) {
        MultiItemEntity entity = mDatas.get(position);
        if (entity instanceof GankContent) {
            GankContent content = (GankContent) entity;
            ActivityUtil.gotoDetailActivity(getContext(), content.getUrl(), content.getDesc());
            mPresenter.addOneHis(content, content.getType());
        } else if (entity instanceof GankSection) {
            gankRecommendlistRl.post(() -> {
                gankRecommendlistRl.smoothScrollToPosition(0);
                handler.sendEmptyMessage(START_REFRESH);
            });
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_REFRESH:
                    mPresenter.getCommonRecommend(User.getInstance(), getContext(), true);
                    break;
            }
        }
    };


    @Override
    public void setRecommendTagsData(List<Tag> tags) {
        gankRecommendSrl.setEnabled(false);
        recommendSearchFab.setVisibility(View.GONE);

        TagAdapter adapter = new TagAdapter(getContext(), R.layout.gank_tag_item, tags);
        gankRecommendlistRl.setLayoutManager(new GridLayoutManager(getContext(), 4));
        gankRecommendlistRl.setAdapter(adapter);
        adapter.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.recycler_header,
                (ViewGroup) gankRecommendlistRl.getParent(), false));
        adapter.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.tag_recycler_header,
                (ViewGroup) gankRecommendlistRl.getParent(), false));

        adapter.addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.tag_recycler_footer,
                (ViewGroup) gankRecommendlistRl.getParent(), false));
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Toast.makeText(getContext(), tags.get(position).getType(), Toast.LENGTH_SHORT).show();
        });

        adapter.getFooterLayout().findViewById(R.id.tag_selected_bt).setOnClickListener(v -> {

            List<Tag> selectedTags = adapter.getSelectTags();

            if (selectedTags.size() < 3) {

                Toast.makeText(getContext(), R.string.atLeastTagTips, Toast.LENGTH_SHORT).show();

            } else {

                mPresenter.saveLocalTags(getContext(), selectedTags);
                mPresenter.getCommonRecommend(User.getInstance(), getContext(), false);

            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFrgListener)
            homeFrgListener = (HomeFrgListener) context;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
