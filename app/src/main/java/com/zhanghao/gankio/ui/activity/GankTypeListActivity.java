package com.zhanghao.gankio.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.contract.GankContract;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.listener.LikeListener;
import com.zhanghao.gankio.listener.RecyclerScrollListener;
import com.zhanghao.gankio.model.GankDataRemoteRepository;
import com.zhanghao.gankio.presenter.GankRemotePresenter;
import com.zhanghao.gankio.ui.adapter.GankTypeAdapter;
import com.zhanghao.gankio.ui.widget.CustomLoadMore;
import com.zhanghao.gankio.util.ActivityUtil;
import com.zhanghao.gankio.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghao on 2017/4/30.
 */

public class GankTypeListActivity extends BaseToolbarActivity<GankContract.TypePresenter> implements GankContract.TypeView, LikeListener {
    private static final String TAG = "GankTypeListActivity";
    @BindView(R.id.ganktype_rl)
    RecyclerView ganktypeRl;
    @BindView(R.id.ganktype_srl)
    SwipeRefreshLayout ganktypeSrl;
    @BindView(R.id.load_failed_tv)
    TextView loadFailedTv;
    @BindView(R.id.load_failed_ll)
    LinearLayout loadFailedLl;
    private String mType;
    private int page = 1;
    private List<MultiItemEntity> mDatas = new ArrayList<>();
    private GankTypeAdapter gankTypeAdapter;
    private boolean LIKE_CLICK = true;


    @Override
    protected int setContentLayout() {
        return R.layout.ganktype_layout;
    }

    @Override
    protected boolean canBack() {
        return true;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntentData();
        setTitle(mType);
        initView();
    }

    private void initView() {
        ganktypeSrl.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);
        ganktypeSrl.setProgressViewOffset(true, 100, 200);
        ganktypeSrl.setOnRefreshListener(() -> {
            page = 1;
            mPresenter.getTypeData(mType, String.valueOf(page), true, false);
        });
        ganktypeRl.addOnScrollListener(listener);

        loadFailedTv.setOnClickListener(v->{
             mPresenter.getTypeData(mType, String.valueOf(1), false, false);
            loadFailedLl.setVisibility(View.GONE);
        });
    }


    private RecyclerScrollListener listener = new RecyclerScrollListener() {
        @Override
        public void hideToolBar() {
            if (mToolbar.isShow())
                mToolbar.hide();
        }

        @Override
        public void showToolBar() {
            if (!mToolbar.isShow())
                mToolbar.show();
        }

        @Override
        public void hideBottomBar() {

        }

        @Override
        public void showBottomBar() {

        }

        @Override
        public void firstVisibleItemPosition(int position) {

        }
    };


    private void initIntentData() {
        Intent intent = getIntent();
        mType = intent.getStringExtra("type");
        new GankRemotePresenter(this, GankDataRemoteRepository.getInstance());
        mPresenter.getTypeData(mType, String.valueOf(page), false, false);
    }

    @Override
    public void showDialog() {
        if (!ganktypeSrl.isRefreshing())
            ganktypeSrl.post(() -> {
                ganktypeSrl.setRefreshing(true);
            });
    }

    @Override
    public void hideDialog() {
        if (ganktypeSrl.isRefreshing())
            ganktypeSrl.post(() -> {
                ganktypeSrl.setRefreshing(false);
            });
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (ganktypeSrl.isRefreshing())
            ganktypeSrl.post(() -> {
                ganktypeSrl.setRefreshing(false);
            });
        LIKE_CLICK = true;

        loadFailedLl.setVisibility(View.VISIBLE);

    }

    @Override
    public void setPresenter(GankContract.TypePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setUpTypeData(List<MultiItemEntity> datas, boolean isRefresh, boolean isLoadMore) {

        if (!isRefresh && !isLoadMore) {
            mDatas.addAll(datas);
            gankTypeAdapter = new GankTypeAdapter(mDatas, this);
            gankTypeAdapter.addHeaderView(LayoutInflater.from(this).inflate(R.layout.recycler_header,
                    (ViewGroup) ganktypeRl.getParent(), false));


            LogUtil.d(TAG, datas.get(0).getItemType());

            if (datas.get(0).getItemType() == Constant.IMG)
                ganktypeRl.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            else
                ganktypeRl.setLayoutManager(new LinearLayoutManager(this));

            gankTypeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            gankTypeAdapter.isFirstOnly(true);

            ganktypeRl.setAdapter(gankTypeAdapter);

            gankTypeAdapter.setLoadMoreView(new CustomLoadMore());
            gankTypeAdapter.setOnLoadMoreListener(() -> {
                ++page;
                mPresenter.getTypeData(mType, String.valueOf(page), false, true);
            }, ganktypeRl);
            gankTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
                onItemClick(position);
            });
        }

        if (isRefresh) {
            mDatas.clear();
            mDatas.addAll(datas);
            gankTypeAdapter.notifyDataSetChanged();
            if (ganktypeSrl.isRefreshing())
                ganktypeSrl.setRefreshing(false);
        }

        if (isLoadMore) {
            mDatas.addAll(datas);
            gankTypeAdapter.loadMoreComplete();
        }


    }

    private void onItemClick(int position) {
        GankContent content = (GankContent) mDatas.get(position);

        //加入历史记录

        addOneItemToHistory(content);

        if (mDatas.get(position).getItemType() == Constant.IMG) {
            ActivityUtil.gotoPhotoActivity(this, mDatas, position);
        } else {
            ActivityUtil.gotoDetailActivity(this, content.getUrl(), content.getDesc());
        }

    }


    private void addOneItemToHistory(GankContent item){
        String token = User.getInstance().getUserToken();
        if (token!=null && !token.isEmpty()){
            mPresenter.addOneHis(item,token);
        }
    }



    @Override
    public void onAddFavSuccess(String message, int pos) {
        LIKE_CLICK = true;
        GankContent content = (GankContent) mDatas.get(pos);
        content.setFav(true);
        gankTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteFavSuccess(String message, int pos) {
        LIKE_CLICK = true;
        GankContent content = (GankContent) mDatas.get(pos);
        content.setFav(false);
        gankTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLiked(int pos) {

        if (LIKE_CLICK) {
            String token = User.getInstance().getUserToken();
            if (!token.isEmpty()) {
                GankContent content = (GankContent) mDatas.get(pos);
                mPresenter.addOneFav(content, token, pos);
                LIKE_CLICK = false;
            } else {
                Toast.makeText(this, "请登录", Toast.LENGTH_SHORT).show();
                gankTypeAdapter.notifyDataSetChanged();
            }

        }

    }

    @Override
    public void onUnLiked(int pos) {
        GankContent content = (GankContent) mDatas.get(pos);
        mPresenter.deleteOneFav(User.getInstance(), content.get_id(), pos);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
