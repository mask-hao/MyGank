package com.zhanghao.gankio.ui.fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.contract.GankContract;
import com.zhanghao.gankio.entity.MoreEntity;
import com.zhanghao.gankio.model.GankDataRepository;
import com.zhanghao.gankio.presenter.GankPresenter;
import com.zhanghao.gankio.ui.adapter.MoreAdapter;
import com.zhanghao.gankio.ui.widget.DividerItemDecoration;
import com.zhanghao.gankio.util.ActivityUtil;
import com.zhanghao.gankio.util.LogUtil;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhanghao on 2017/3/17.
 */

public class MoreFragment extends BaseFragment<GankContract.MorePresenter> implements GankContract.MoreView{
    private static final String TAG = "MoreFragment";
    @BindView(R.id.gankmore_rl)
    RecyclerView gankmoreRl;
    Unbinder unbinder;
    private List<MoreEntity> moreEntities=new ArrayList<>();
    private MoreAdapter moreAdapter;
    private ItemTouchHelper itemTouchHelper;
    private ItemDragAndSwipeCallback dragAndSwipeCallback;

    public static MoreFragment getInstance(){
        MoreFragment moreFragment=new MoreFragment();
        new GankPresenter(moreFragment, GankDataRepository.getInstance());
        return moreFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = getRoot(inflater, container);
        unbinder = ButterKnife.bind(this, root);
        mPresenter.getMoreData(getContext());
        return root;
    }


    private OnItemDragListener dragListener=new OnItemDragListener() {
        @Override
        public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
        }

        @Override
        public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
            LogUtil.d(TAG,"from: "+from);
            LogUtil.d(TAG,"to: "+to);
        }

        @Override
        public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
            mPresenter.updateMoreData(getContext(),moreEntities);
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void setPresenter(GankContract.MorePresenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public void setUpMoreData(List<MoreEntity> datas) {

        moreEntities.addAll(datas);

        gankmoreRl.setLayoutManager(new GridLayoutManager(getContext(), 2));
        moreAdapter = new MoreAdapter(R.layout.gankmoreitem_layout, moreEntities, getContext());
        moreAdapter.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.recycler_header,
                (ViewGroup) gankmoreRl.getParent(), false));
        moreAdapter.addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.recycler_footer,
                (ViewGroup) gankmoreRl.getParent(), false));

        moreAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        moreAdapter.isFirstOnly(true);

        dragAndSwipeCallback=new ItemDragAndSwipeCallback(moreAdapter);
        itemTouchHelper=new ItemTouchHelper(dragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(gankmoreRl);
        dragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);

        moreAdapter.enableDragItem(itemTouchHelper);
        moreAdapter.setOnItemDragListener(dragListener);
        gankmoreRl.setAdapter(moreAdapter);
        moreAdapter.setOnItemClickListener((adapter, view, position) -> {
            String mType = moreEntities.get(position).getType();
            ActivityUtil.gotoTypeListActivity(getContext(), mType);
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
