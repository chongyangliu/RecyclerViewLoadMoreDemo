package comm.example.hongentao.recyclerviewloadmore;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongentao on 16/3/10.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyHolder> {
    private Context mContext;
    private OnLoadMoreListener mOnLoadMoreListener;
    public List<String> mList = new ArrayList<>();
    public static final int LOADMORE = 1;
    public static final int NORMAL = 2;
    private int visibleItemCount;
    private int totalItemCount;
    private int firstVisibleItem;
    private boolean loading = false;
    private boolean isLoadMore = true;

    public TestAdapter(Context context, RecyclerView recyclerView) {
        this.mContext = context;
        final LinearLayoutManager mLayoutManager;
        if (recyclerView != null && recyclerView.getLayoutManager() instanceof
                LinearLayoutManager) {
            mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    // check for scroll down
                    if (isLoadMore) {
                        if (dy > 0) {
                            visibleItemCount = mLayoutManager.getChildCount();
                            totalItemCount = mLayoutManager.getItemCount();
                            firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                            if (!loading) {
                                if (totalItemCount <= visibleItemCount + firstVisibleItem) {
                                    if (mOnLoadMoreListener != null) {
                                        mOnLoadMoreListener.loadMore();
                                    }
                                    loading = true;
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 设置下拉加载更多功能
     *
     * @param isLoadMore
     */
    public void setLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    /**
     * reset loading status
     */
    public void setLoaded() {
        loading = false;
    }


    public void addOnLoadMoreListener(OnLoadMoreListener mLoadMoreListener) {
        this.mOnLoadMoreListener = mLoadMoreListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = null;
        switch (viewType) {
            case LOADMORE:
                mView = LayoutInflater.from(mContext).inflate(R.layout.progressbar_item, null);
                break;
            case NORMAL:
                mView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
                break;
        }
        return new MyHolder(mView, viewType);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if (mList.get(position) == null) {
            holder.mProgressBar.setIndeterminate(true);
        } else {
            holder.mTextView.setText(mList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) == null) {
            return LOADMORE;
        } else {
            return NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ProgressBar mProgressBar;

        public MyHolder(View itemView, int viewType) {
            super(itemView);
            init(itemView, viewType);
        }

        private void init(View view, int viewType) {
            switch (viewType) {
                case LOADMORE:
                    mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
                    break;
                case NORMAL:
                    mTextView = (TextView) view.findViewById(R.id.textview);
                    break;
                default:
                    break;
            }
        }
    }
}
