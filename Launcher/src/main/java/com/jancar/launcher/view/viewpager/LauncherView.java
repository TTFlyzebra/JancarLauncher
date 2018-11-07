package com.jancar.launcher.view.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jancar.launcher.bean.CellBean;
import com.jancar.launcher.bean.PageBean;
import com.jancar.launcher.view.pageview.SimplePageView;

import java.util.ArrayList;
import java.util.List;

public class LauncherView extends ViewPager implements ILauncher {
    private List<PageBean> pageList = new ArrayList<>();
    private MyPgaeAdapter myPgaeAdapter = new MyPgaeAdapter();

    public LauncherView(Context context) {
        this(context, null);
    }

    public LauncherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setAdapter(myPgaeAdapter);
    }


    @Override
    public void setData(PageBean pageBean) {
        List<CellBean> pages = pageBean.cells;
        int size = pageBean.cells.size();
        int pageNum = pageBean.columns * pageBean.rows;
        List<PageBean> tmpList = new ArrayList<>();
        for (int i = 0; i <= (size - 1) / pageNum; i++) {
            int start = i * pageNum;
            int end = Math.min(size, i * pageNum + pageNum);
            List<CellBean> list = new ArrayList<>(pages.subList(start, end));
            try {
                PageBean page = pageBean.clone();
                page.cells.addAll(list);
                tmpList.add(page);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        if (tmpList.size() > 1) {
            pageList.add(tmpList.get(tmpList.size() - 1));
            pageList.addAll(tmpList);
            pageList.add(tmpList.get(0));
        } else {
            pageList.addAll(tmpList);
        }
        setCurrentItem(1);
        myPgaeAdapter.notifyDataSetChanged();
    }

    public class MyPgaeAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageList == null ? 0 : pageList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SimplePageView simplePageView = new SimplePageView(getContext());
            simplePageView.setData(pageList.get(position));
            container.addView(simplePageView);
            return simplePageView;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (position == 0 && pageList != null && pageList.size() > 1) {
                setCurrentItem(pageList.size() - 2, false);
            }
            if (position == pageList.size() - 1 && pageList != null && pageList.size() > 1) {
                setCurrentItem(1, false);
            }
        }

    }


}
