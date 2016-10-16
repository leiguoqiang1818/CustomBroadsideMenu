package com.example.custombroadsidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
/**
 * 自定义侧滑菜单控件，实现效果模仿QQ侧滑菜单
 * 实现原理：自定义一个类继承viewgrounp，重写onlayout（）和ontouchEnvent（）方法
 * 准备好两个布局文件，一个左边菜单，一个主布局，在新建一个布局（本自定义的控件进行），将这两个布局include到该自定义控件中，
 * 将左边菜单放置于屏幕左外边，主布局全部显示在屏幕上，然后再重新写ontouchEnvent（）对两个子布局进行不同的显示
 * @author wsd_leiguoqiang
 */
public class CustomBroadsideMenu extends ViewGroup{
	/**
	 * 按下时候的x值
	 */
	private float downx;
	/**
	 * 按下时候的y值
	 */
	private float downy;
	/**
	 * 平滑动画模拟对象
	 */
	private Scroller scroller;
	/**
	 * 标记变量，标记viewpager当前显示元素的下标值，用于判断是否拦截手势事件
	 * 当值为0时，就进行事件拦截
	 */
	private int flag_viewpager_index;
	/**
	 * 标记标量，标记当前显示的布局是否为main_layout,true表示是，false表示否
	 */
	private boolean flag_main_layout = true;

	public boolean isFlag_main_layout() {
		return flag_main_layout;
	}

	public void setFlag_main_layout(boolean flag_main_layout) {
		this.flag_main_layout = flag_main_layout;
	}

	public int getFlag_viewpager_index() {
		return flag_viewpager_index;
	}

	public void setFlag_viewpager_index(int flag_viewpager_index) {
		this.flag_viewpager_index = flag_viewpager_index;
	}

	public CustomBroadsideMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomBroadsideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomBroadsideMenu(Context context) {
		super(context);
		init();
	}
	/**
	 * 自定义方法，初始化变量
	 */
	private void init() {
		scroller = new Scroller(getContext());
	}
	/**
	 * 重写拦截手势事件，当手势事件触发时，被调用
	 * 
	 * 解决冲突1：viewpager左右滑动和本控件的左右滑动
	 * 实现原理：
	 * 1）当处于主界面（main_layout）
	 * 2）并且viewpager显示的元素为第一个
	 * 3）手势方向为向右滑动
	 * 当满足上面三个条件时候，进行手势事件的向下拦截，从而达到控件本身的消费
	 * 
	 * 解决冲突2：当处于显示左边布局的时候，本布局控件里的子控件与本控件的左右滑动冲突
	 * 实现原理：
	 * 1）显示界面处于左侧布局（left_menu）
	 * 2)左右滑动距离>上下滑动距离，判定是左右滑动
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downx = ev.getX();
			downy = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float movex = ev.getX();
			float movey = ev.getY();
			//x方向的位移，>0（向右滑动），反之向左滑动
			float movedx_value = movex-downx;
			//x和y方向的距离，用于解决冲突2
			float movex_value = Math.abs(movex-downx);
			float movey_value = Math.abs(movey-downy);
			//同时满足3个条件，解决冲突1
			if(flag_main_layout&&(flag_viewpager_index==0)&&(movedx_value>0f)){
				return true;
			}
			//同时满足下面3个条件，解决冲突2，与2比较，是为了防止收抖动带来良好的体验
			if(!flag_main_layout&&(movex_value>movey_value)&&(movex_value>2)){
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:

			break;

		}
		return super.onInterceptTouchEvent(ev);
	}
	/**
	 * 测量两个子布局并设置宽高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//获取第一个子控件
		View left_menu = getChildAt(0);
		//获取第二个子控件
		View main_layout = getChildAt(1);
		//定位第一个子控件位置
		left_menu.measure(-left_menu.getLayoutParams().width, heightMeasureSpec);
		//定位第二个子控件位置
		main_layout.measure(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	/**
	 * 指定两个子布局的具体位置
	 * int l, 控件本身离屏幕左边的距离
	 *  int t, 控件本身离屏幕上边的距离
	 *  int r, 控件本身离屏幕右边的距离
	 *  int b, 控件本身离屏幕下边的距离
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//指定左边布局
		getChildAt(0).layout(-getChildAt(0).getLayoutParams().width, 0, 0, b);
		// 指定右边布局
		getChildAt(1).layout(l, t, r, b);
	}
	/**
	 * 重写手势方法，进行左右滑动控制显示还是隐藏
	 * 实现效果：
	 * 1）手势左右滑动屏幕，屏幕会随着手势显示到整个控件布局响应的位置
	 * 2）认为指定规则，当左右布局滑动到一定的距离时，进行自动补全布局完全显示
	 * 3）自动补全显示操作，运用平滑器，进行处理
	 * 注意：scrollBy(当前坐标+参数值=最后需要移动到的坐标位置)方法和scrollTo(直接移动到参数所指定的坐标位置)方法控制的是屏幕的移动方向
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//记录按下时候的x的值
			downx = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			//记录移动时候的x值
			Float movex = event.getX();
			//获取按下与移动后两者之间的相反位移，因为方法scrollby()方法控制的是屏幕的具体位置
			//故我们需要显示的方向效果与实际手势的滑动位移方向是相反的
			//获取两点之间的反向位移
			int scrollx = (int)(downx-movex);
			//获取即将得到的x的坐标值=之前的x值+当前的x的移动量
			int new_scroll_positon = getScrollX()+scrollx;
			//设置移动的界限，不能超出左右的布局的边界
			if(new_scroll_positon<-getChildAt(0).getLayoutParams().width){
				//滑动到最左边界限处
				scrollTo(-getChildAt(0).getLayoutParams().width, 0);
				//如果计算出的结果超出了正常的（0，0）显示，则保持正常显示
			}else if(new_scroll_positon>0){
				//将屏幕正常显示
				scrollTo(0, 0);
				//最后一种情况，屏幕滑动范围处于两个边界之间
			}else{
				//直接显示移动的结果
				scrollBy(scrollx, 0);
			}
			//关键地方：将movex直接赋值给downx，目的：使每次scrollx的值为当前movex与上一次movex之间的即时移动值
			downx = movex;
			break;
			//设置显示内容自动补全操作，当移动的显示的宽度操作左边布局的一般的宽度的时候，就进行自动补全显示操作
		case MotionEvent.ACTION_UP:
			//获取左边布局的宽度的一般的值
			int left_point = -(int)(getChildAt(0).getLayoutParams().width/2.0f);
			//当前x的坐标值与-左边一半比较
			if(getScrollX()<left_point){
				//补全显示操作
				updateCurrentState(true);
			}else{
				//恢复正常显示操作
				updateCurrentState(false);
			}
			break;

		}
		//应为是完全自定义控件，所以这里直接用true，直接消费掉该手势操作
		//如果是继承某个原生的控件，则需要进行super（）父类的方法，否则某些功能会丢失
		return true;
	}
	/**
	 * 自定义方法，进行自动显示补全操作
	 * @param flag：true为自动补全，false为恢复正常显示
	 * 平滑移动实现原理：创建scroller对象，调用startScroll（）方法，进行平滑动作，然后当前控件对象调用invaledite（）方法，
	 * 进行控件的重绘操作，并且需要重写computeScroll()方法，进行循环式的维持平滑动画
	 */
	private void updateCurrentState(boolean flag) {
		//初始化x移动变量
		int dx = 0;
		if(flag){
			//显示左边的布局，这里使用平滑动画技术，显得很自然，直接用scrollto（）方法，可以达到效果，但是效果很生硬
			dx = -getChildAt(0).getLayoutParams().width-getScrollX();
			//改变现实布局标记标量的值
			setFlag_main_layout(false);
		}else{
			//隐藏左边的布局
			dx = 0-getScrollX();
			//改变现实布局标记标量的值
			setFlag_main_layout(true);
		}
		//进行绝对值话，根据滑动的距离获取不同的时间，保持平滑动作的速度一致性
		int duration = Math.abs(dx*10);
		//开启scroller平滑动画
		//		scroller.startScroll(startX(开始x坐标), startY(开始y坐标), dx(x方向移动的量), dy（y方向的移动两）, duration（持续时长）);
		scroller.startScroll(getScrollX(), 0, dx, 0, duration);
		//调用控件重绘方法
		//该方法底层原理：invalidate（）--》drawchild（）--》computeScroll（）
		invalidate();
	}
	/**
	 * 维持平滑移动的方法
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();
		//为true时候，说明模拟动画还没有结束，反之结束
		if(scroller.computeScrollOffset()){
			//从scroller模拟中获取当前的x值
			int currentx = scroller.getCurrX();
			//滑动到指定的位置
			scrollTo(currentx, 0);
			//调用控件的invalidate（）方法，进行循环重绘，知道模拟器动画数据结束
			invalidate();
		}
	}
}
