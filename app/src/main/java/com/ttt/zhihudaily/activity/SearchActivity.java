package com.ttt.zhihudaily.activity;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.adapter.MyListAdapter;
import com.ttt.zhihudaily.adapter.MySearchHistoryListAdapter;
import com.ttt.zhihudaily.db.DBUtil;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.util.HttpUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private DBUtil mDBUtil;
    private ImageView searchCancel;
    private EditText editText;
    private ImageView searchStart;
    private MyListAdapter titleAdapter;
    private MySearchHistoryListAdapter historyAdapter;
    private ListView listView;
    private TextView textFail;
    private List<Title> titleList;
    private List<String> historyList;
    private String[] autoCompleteList = {"自", "动", "完", "成", "列", "表"};
    private Drawable divider;
    private View footerView;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        setEditText();

        showHistoryList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_start:
                search();
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                break;
            case R.id.search_cancel:
                editText.setText("");
                // 重新打开软键盘
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            default:
                break;
        }
    }

    private void init() {
        editText = (EditText) findViewById(R.id.search_edit_text);
        searchCancel = (ImageView) findViewById(R.id.search_cancel);
        searchStart = (ImageView) findViewById(R.id.search_start);
        textFail = (TextView) findViewById(R.id.search_fail_text);
        listView = (ListView) findViewById(R.id.list_view_search);

        searchCancel.setOnClickListener(this);
        searchStart.setOnClickListener(this);

        titleList = new ArrayList<>();
        historyList = new ArrayList<>();
        titleAdapter = new MyListAdapter(this, R.layout.list_view_item, titleList);
        footerView = LayoutInflater.from(this).inflate(R.layout.list_view_footer, null);
        historyAdapter = new MySearchHistoryListAdapter(this, R.layout.serach_history_list_view_item, historyList);
        // 点叉叉删除搜索记录后，重新显示列表
        historyAdapter.setDeleteHistoryListener(new MySearchHistoryListAdapter.DeleteHistoryListener() {
            @Override
            public void onDeleteFinish() {
                historyList.clear();
                historyList.addAll(mDBUtil.loadSearchHistory());
                Collections.reverse(historyList);
                if (historyList.size() == 0) {
                    listView.removeFooterView(footerView);
                }
                historyAdapter.notifyDataSetChanged();
            }
        });

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    private void setEditText() {
        // 设置按下软键盘Enter键执行搜索
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_SEARCH){
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    search();
                }
                return true;
            }
        });

        // 监听EditText输入内容变化
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textFail.setVisibility(View.GONE);
                if (!"".equals(s.toString())) {
                    searchCancel.setVisibility(View.VISIBLE);
                    searchStart.setVisibility(View.VISIBLE);
                    showAutoCompleteList();
                } else {
                    searchCancel.setVisibility(View.GONE);
                    searchStart.setVisibility(View.GONE);
                    showHistoryList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // 执行搜索
    private void search() {
        String key = editText.getText().toString();
        if (!key.equals("")) {
            if (mDBUtil.isExistInSearch(key)) {
                mDBUtil.deleteSearchHistory(key);
            }
            mDBUtil.saveSearchHistory(key);
            titleList.clear();
            titleList.addAll(mDBUtil.searchNewsTitle(key));
            showTitleList();
            if (titleList.size() == 0) {
                textFail.setVisibility(View.VISIBLE);
            }
        }
    }

    // 显示搜索结果列表
    private void showTitleList() {
        listView.setDivider(null);
        if (footerView != null) {
            listView.removeFooterView(footerView);
        }
        listView.setAdapter(titleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (HttpUtil.isNetworkConnected(SearchActivity.this)) {
                    NewsActivity.startNewsActivity(SearchActivity.this, titleAdapter.getItem(position));
                } else {
                    Snackbar.make(listView, "没有网络", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 显示搜索历史列表
    private void showHistoryList() {
        if (divider == null) {
            divider = listView.getDivider();
        }
        mDBUtil = DBUtil.getInstance(this);
        historyList.clear();
        historyList.addAll(mDBUtil.loadSearchHistory());
        Collections.reverse(historyList);
        listView.setDivider(divider);
        if (listView.getFooterViewsCount() == 0 && historyList.size() != 0) {
            // 增加“清除搜索记录”页脚
            listView.addFooterView(footerView, null, true);
        }
        listView.setAdapter(historyAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 点击最后一项清除搜索记录
                if (position == historyList.size()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SearchActivity.this);
                    dialog.setMessage("确定删除所有搜索记录？");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDBUtil.deleteAllSearchHistory();
                            historyList.clear();
                            historyAdapter.notifyDataSetChanged();
                            listView.removeFooterView(footerView);
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                } else {
                    // 把关键词放入EditText，并执行搜索
                    editText.setText(historyAdapter.getItem(position));
                    search();
                }
            }
        });
    }

    // 显示自动完成列表
    private void showAutoCompleteList() {
        listView.setDivider(divider);
        if (footerView != null) {
            listView.removeFooterView(footerView);
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    // 触碰屏幕就关闭软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        return super.dispatchTouchEvent(ev);
    }
}
