package comm.example.hongentao.recyclerviewloadmore;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongentao on 16/3/10.
 */
public class TestActivity extends Activity {
    private RecyclerView mRecyclerView;
    private TestAdapter mTestAdapter;
    private LinearLayoutManager mLayoutManager;
    private Handler myHandler;
    private SwipeRefreshLayout mSwipRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        init();
    }


    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mSwipRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiprefreshlayout);
    }

    private void init() {
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mTestAdapter = new TestAdapter(this, mRecyclerView);
        final List<String> mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add("haha");
        }
        mTestAdapter.mList.clear();
        mTestAdapter.mList.addAll(mList);
        mTestAdapter.setLoadMore(true);
        mTestAdapter.addOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                mTestAdapter.mList.add(null);
                mTestAdapter.notifyItemInserted(mTestAdapter.mList.size() - 1);
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTestAdapter.mList.remove(mTestAdapter.mList.size() - 1);
                        mTestAdapter.notifyItemRemoved(mTestAdapter.mList.size());
                        for (int i = 0; i < 30; i++) {
                            mTestAdapter.mList.add("haha");
                            mTestAdapter.notifyItemInserted(mTestAdapter.mList.size());
                        }
                        mTestAdapter.setLoaded();
                    }
                }, 2 * 1000);
            }
        });

        mSwipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mTestAdapter.mList.clear();
                final List<String> mList = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    mList.add("haha");
                }
                mTestAdapter.mList.addAll(mList);
                mTestAdapter.notifyDataSetChanged();
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipRefreshLayout.setRefreshing(false);
                    }
                }, 2 * 1000);
            }
        });
        mRecyclerView.setAdapter(mTestAdapter);
    }
}
