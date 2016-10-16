package com.example.custombroadsidemenu;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.example.custombroadsidemenu.fragment.FragmentFirst;
import com.example.custombroadsidemenu.fragment.FragmentFourth;
import com.example.custombroadsidemenu.fragment.FragmentSeconde;
import com.example.custombroadsidemenu.fragment.FragmentThird;

public class MainActivity extends FragmentActivity implements OnPageChangeListener{
	private ViewPager view_pager;
	/**
	 * 自定义布局控件对象
	 */
	private CustomBroadsideMenu broadsideMenu;
	private FragmentFirst fragment_01 = new FragmentFirst();
	private FragmentSeconde fragment_02 = new FragmentSeconde();
	private FragmentThird fragment_03 = new FragmentThird();
	private FragmentFourth fragment_04 = new FragmentFourth();
	private List<Fragment> fragments = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragments.add(fragment_01);
		fragments.add(fragment_02);
		fragments.add(fragment_03);
		fragments.add(fragment_04);
		broadsideMenu = (CustomBroadsideMenu) findViewById(R.id.custom_broadside_menu);
		view_pager = (ViewPager) findViewById(R.id.view_pager);
		ViewPagerAdaper adapter = new ViewPagerAdaper(getSupportFragmentManager(), fragments);
		view_pager.setAdapter(adapter);
		view_pager.setOnPageChangeListener(this);
	}
	/**
	 * viewpager的adapter
	 * 如果里面放的是fragment，则继承fragmentpagerAdapter
	 * 如果里面放的是imagerview或是其他的控件，则继承pagerAdapter
	 * @author wsd_leiguoqiang
	 */
	class ViewPagerAdaper extends FragmentPagerAdapter{
		/**
		 * 数据源
		 */
		private List<Fragment> fragments;

		public ViewPagerAdaper(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}
	/**
	 * viewpager控件的pager改变监听
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	/**
	 * pager已经改变时候回调，进行customBoraodsideMenu对象viewpager下标值的设置
	 * 为是否拦截手势事件做准备
	 */
	@Override
	public void onPageSelected(int arg0) {
		//设置viewpager当前显示元素的下标值
		broadsideMenu.setFlag_viewpager_index(arg0);
		System.out.println("viewpager下标值：：："+arg0);
		System.out.println("对象中的viewpager下标值：：："+broadsideMenu.getFlag_viewpager_index());
	}
}
