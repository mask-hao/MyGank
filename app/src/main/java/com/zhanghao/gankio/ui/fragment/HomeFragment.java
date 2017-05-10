package com.zhanghao.gankio.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.contract.GankContract;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.GankSection;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.listener.HomeFrgListener;
import com.zhanghao.gankio.listener.LikeListener;
import com.zhanghao.gankio.listener.RecyclerScrollListener;
import com.zhanghao.gankio.model.GankDataRepository;
import com.zhanghao.gankio.presenter.GankPresenter;
import com.zhanghao.gankio.ui.adapter.HomeAdapter;
import com.zhanghao.gankio.ui.widget.CustomLoadMore;
import com.zhanghao.gankio.ui.widget.MyFloatingActionButton;
import com.zhanghao.gankio.util.ActivityUtil;
import com.zhanghao.gankio.util.ComUtil;
import com.zhanghao.gankio.util.FragmentUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by zhanghao on 2017/3/17.
 */

public class HomeFragment extends BaseFragment<GankContract.DailyPresenter> implements GankContract.DailyView, LikeListener {
    private static final String TAG = "HomeFragment";
    Unbinder unbinder;
    @BindView(R.id.ganklist_lv)
    RecyclerView gankDailyLv;
    @BindView(R.id.gankhome_srl)
    SwipeRefreshLayout gankhomeSrl;
    @BindView(R.id.home_search_fab)
    MyFloatingActionButton homeSearchFab;
//    private GankContract.DailyPresenter dailyPresenter;
    private HomeAdapter homeDataAdapter;
    private List<MultiItemEntity> mDatas = new ArrayList<>();
    private List<Integer> dateList = new ArrayList<>();
    private int curStatusCode;
    private static int page = 1;
    private HomeFrgListener homeFrgListener;
    public static String currentTitle = "主页";
    private List<GankSection> gankSections = new ArrayList<>();
    private boolean LIKE_CLICK = true;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }


    public static HomeFragment getInstance(){
        HomeFragment homeFragment=new HomeFragment();
        new GankPresenter(homeFragment, GankDataRepository.getInstance());
        return homeFragment;
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
        homeSearchFab.setOnClickListener(v->{
            ActivityUtil.gotoSearchActivity(getContext());
        });
        gankhomeSrl.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);
        gankhomeSrl.setProgressViewOffset(true, 100, 200);
        gankhomeSrl.setOnRefreshListener(() -> {
            page = 1;
            mPresenter.getDailyData(String.valueOf(page), true, false);
        });
        gankDailyLv.setLayoutManager(new LinearLayoutManager(getContext()));
        gankDailyLv.addOnScrollListener(recyclerScrollListener);
        if (mPresenter != null)
            mPresenter.getDailyData(String.valueOf(page), false, false);
        dateList.add(0);
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
            if (!homeSearchFab.isHidden())
                homeSearchFab.hide();
        }

        @Override
        public void showBottomBar() {
            if (homeFrgListener != null)
                homeFrgListener.showBottomBar();
            if (homeSearchFab.isHidden())
                homeSearchFab.show();
        }

        @Override
        public void firstVisibleItemPosition(int position) {
            if (homeFrgListener != null)
                setToolbarTitle(position);
        }
    };

    private void setToolbarTitle(int position) {

//        LogUtil.d(TAG, "pos: " + String.valueOf(position));
//        LogUtil.d(TAG,"cur: "+String.valueOf(curStatusCode));

        MultiItemEntity itemEntity = mDatas.get(position);
        if (itemEntity instanceof GankSection && itemEntity.getItemType() == Constant.IMG) {
            GankSection section = (GankSection) itemEntity;
            String date = ComUtil.getFormatDate(section.getContent().getPublishedAt());
//            LogUtil.d(TAG,date);
            currentTitle = date;
            if (date != null && !date.isEmpty()) {
                homeFrgListener.setToolbarTitle(date);
                curStatusCode = position;
            }
        }

        if (position < curStatusCode) {

            int index = dateList.indexOf(curStatusCode);
            if (index != -1) {
                ListIterator<Integer> iterator = dateList.listIterator(index);
                if (iterator.hasPrevious()) {
                    int pre = iterator.previous();
                    int type = mDatas.get(pre).getItemType();
                    if (type == Constant.IMG) {
                        GankSection gankSection = (GankSection) mDatas.get(pre);
                        String date1 = gankSection.getContent().getPublishedAt();
                        currentTitle = date1;
                        homeFrgListener.setToolbarTitle(ComUtil.getFormatDate(date1));
                    }
                }
            }
        }
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


    @Override
    public void showDialog() {
        if (!gankhomeSrl.isRefreshing())
            gankhomeSrl.post(() -> {
                gankhomeSrl.setRefreshing(true);
            });
    }

    @Override
    public void hideDialog() {
        if (gankhomeSrl.isRefreshing())
            gankhomeSrl.post(() -> {
                gankhomeSrl.setRefreshing(false);
            });
    }

    @Override
    public void showError(String message) {
        LIKE_CLICK = true;
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        if (gankhomeSrl.isRefreshing())
            gankhomeSrl.post(() -> {
                gankhomeSrl.setRefreshing(false);
            });
        if (homeDataAdapter != null)
            homeDataAdapter.loadMoreFail();
    }

    @Override
    public void setPresenter(GankContract.DailyPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setUpDailyData(List<MultiItemEntity> datas, boolean isRefresh, boolean isLoadMore) {
        //首次加载
        if (!isRefresh && !isLoadMore) {
            mDatas.addAll(datas);
            gankSections.add((GankSection) datas.get(0));
            homeDataAdapter = new HomeAdapter(mDatas, this);
            homeDataAdapter.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.recycler_header,
                    (ViewGroup) gankDailyLv.getParent(), false));
            homeDataAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            homeDataAdapter.isFirstOnly(true);
            gankDailyLv.setAdapter(homeDataAdapter);
            homeDataAdapter.setLoadMoreView(new CustomLoadMore());
            homeDataAdapter.setOnLoadMoreListener(() -> {
                ++page;
                mPresenter.getDailyData(String.valueOf(page), false, true);
            }, gankDailyLv);
            homeDataAdapter.setOnItemClickListener((adapter, view, position) -> {
                onItemClick(position);
            });
            dateList.add(mDatas.size());
        }
        //刷新
        if (isRefresh) {
            mDatas.clear();
            mDatas.addAll(datas);
            gankSections.clear();
            gankSections.add((GankSection) datas.get(0));
            dateList.add(mDatas.size());
            homeDataAdapter.notifyDataSetChanged();
        }
        //加载更多
        if (isLoadMore) {
            mDatas.addAll(datas);
            gankSections.add((GankSection) datas.get(0));
            homeDataAdapter.loadMoreComplete();
            dateList.add(mDatas.size());
        }


    }

    private void onItemClick(int position) {
        int type = mDatas.get(position).getItemType();
        if (type == Constant.CONTENT_IMG || type == Constant.CONTENT) {
            GankContent content = (GankContent) mDatas.get(position);
            String url = content.getUrl();
            String title = content.getDesc();
            ActivityUtil.gotoDetailActivity(getContext(), url, title);
        }
        if (type == Constant.IMG) {
            ActivityUtil.gotoPhotoActivityH(getContext(), gankSections, mDatas, position);
        }
    }


    @Override
    public void onAddFavSuccess(String message, int pos) {
        LIKE_CLICK = true;
        GankContent item = (GankContent) mDatas.get(pos);
        item.setFav(true);
        homeDataAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDeleteFavSuccess(String message, int pos) {
        LIKE_CLICK = true;
        GankContent item = (GankContent) mDatas.get(pos);
        item.setFav(false);
        homeDataAdapter.notifyDataSetChanged();

    }


    @Override
    public void onLiked(int pos) {

//        LogUtil.d(TAG,"like"+pos);

        if (LIKE_CLICK) {
            String token = User.getInstance().getUserToken();
            if (!token.isEmpty()) {

//                LogUtil.d(TAG,mDatas.get(pos).getClass().getSimpleName());

                GankContent item = (GankContent) mDatas.get(pos);
                mPresenter.addOneFav(item, token, pos);
                LIKE_CLICK = false;
            } else {
                Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                homeDataAdapter.notifyDataSetChanged();
            }


        }

    }

    @Override
    public void onUnLiked(int pos) {
        if (LIKE_CLICK) {
            GankContent item = (GankContent) mDatas.get(pos);
            mPresenter.deleteOneFav(User.getInstance(), item.get_id(), pos);
            LIKE_CLICK = false;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
