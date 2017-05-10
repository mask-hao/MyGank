package com.zhanghao.gankio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import com.zhanghao.gankio.entity.GankFavContent;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.model.GankDataRepository;
import com.zhanghao.gankio.presenter.GankPresenter;
import com.zhanghao.gankio.ui.adapter.FavAdapter;
import com.zhanghao.gankio.ui.widget.CustomLoadMore;
import com.zhanghao.gankio.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhanghao on 2017/5/4.
 */

public class FavFragment extends BaseFragment<GankContract.FavPresenter> implements GankContract.FavView {

    @BindView(R.id.fav_fag_rl)
    RecyclerView favFagRl;
    Unbinder unbinder;
    @BindView(R.id.fav_frg_srl)
    SwipeRefreshLayout favFrgSrl;
    private static final int count = 10;
    private int page = 1;
    private String mType;
    private List<MultiItemEntity> mDatas = new ArrayList<>();
    private FavAdapter favAdapter;
    private AlertDialog alertDialog;


    private boolean isVisible = false;
    private boolean isDataLoaded = false;
    private boolean isViewInit = false;


    public static FavFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        FavFragment favFragment = new FavFragment();
        new GankPresenter(favFragment, GankDataRepository.getInstance());
        favFragment.setArguments(bundle);
        return favFragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fav_frag_layout;
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
        mType = getArguments().getString("type");
        favFrgSrl.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        favFrgSrl.setEnabled(false);
        favFagRl.setLayoutManager(new LinearLayoutManager(getContext()));
//        mFavPresenter.getFavs(User.getInstance(), mType, String.valueOf(page), String.valueOf(count), false);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        initData(false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInit = true;
        if (getUserVisibleHint()) {
            initData(false);
        }
    }

    private void initData(boolean b) {
        if (isViewInit && isVisible && (!isDataLoaded || b)) {
            mPresenter.getFavs(User.getInstance(), mType, String.valueOf(page), String.valueOf(count), false);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    @Override
    public void showDialog() {
        favFrgSrl.post(() -> {
            favFrgSrl.setEnabled(true);
            favFrgSrl.setRefreshing(true);
        });
    }

    @Override
    public void hideDialog() {
        favFrgSrl.post(() -> {
            favFrgSrl.setEnabled(false);
            favFrgSrl.setRefreshing(false);
        });
    }

    @Override
    public void showError(String message) {
        switch (message) {
            case Constant.USER_INVALID:
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                getActivity().finish();
                break;
            case Constant.NO_MORE_DATA:
                if (favAdapter != null)
                    favAdapter.loadMoreEnd();
                break;
            default:
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void setPresenter(GankContract.FavPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setUpFavs(List<GankFavContent> data, boolean isLoadMore) {
        if (!isLoadMore) {

            System.out.println(data);

            mDatas.addAll(data);
            favAdapter = new FavAdapter(mDatas);

            favAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            favAdapter.isFirstOnly(true);

            favFagRl.setAdapter(favAdapter);
            favAdapter.setLoadMoreView(new CustomLoadMore());
            favAdapter.setOnLoadMoreListener(() -> {
                ++page;
                mPresenter.getFavs(User.getInstance(), mType, String.valueOf(page), String.valueOf(count), false);
            }, favFagRl);

            favAdapter.setOnItemClickListener((adapter, view, position) -> {
                GankFavContent gankFavContent = (GankFavContent) mDatas.get(position);
                ActivityUtil.gotoDetailActivity(getContext(), gankFavContent.getUrl(), gankFavContent.getDesc());
            });


            favAdapter.setOnItemLongClickListener((adapter, view, position) -> {
                alertDialog = new AlertDialog.Builder(getContext())
                        .setCancelable(true)
                        .setMessage(R.string.dodelete)
                        .setNegativeButton(R.string.cancle, (dialog, which) -> {
                            alertDialog.dismiss();
                        })
                        .setPositiveButton(R.string.sure, (dialog, which) -> {
                            GankFavContent gankFavContent = (GankFavContent) mDatas.get(position);
                            mPresenter.deleteOneFav(User.getInstance(), gankFavContent.get_id(), position);
                        })
                        .show();
                return true;
            });

        } else {
            mDatas.addAll(data);
            favAdapter.loadMoreComplete();
        }
        isDataLoaded = true;
    }

    @Override
    public void onAddFavSuccess(String message, int pos) {

    }

    @Override
    public void onDeleteFavSuccess(String message, int pos) {

        System.out.println(mDatas);
        mDatas.remove(pos);
        System.out.println(mDatas);
        favAdapter.notifyDataSetChanged();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
