package com.example.custombroadsidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
/**
 * �Զ���໬�˵��ؼ���ʵ��Ч��ģ��QQ�໬�˵�
 * ʵ��ԭ���Զ���һ����̳�viewgrounp����дonlayout������ontouchEnvent��������
 * ׼�������������ļ���һ����߲˵���һ�������֣����½�һ�����֣����Զ���Ŀؼ����У���������������include�����Զ���ؼ��У�
 * ����߲˵���������Ļ����ߣ�������ȫ����ʾ����Ļ�ϣ�Ȼ��������дontouchEnvent�����������Ӳ��ֽ��в�ͬ����ʾ
 * @author wsd_leiguoqiang
 */
public class CustomBroadsideMenu extends ViewGroup{
	/**
	 * ����ʱ���xֵ
	 */
	private float downx;
	/**
	 * ����ʱ���yֵ
	 */
	private float downy;
	/**
	 * ƽ������ģ�����
	 */
	private Scroller scroller;
	/**
	 * ��Ǳ��������viewpager��ǰ��ʾԪ�ص��±�ֵ�������ж��Ƿ����������¼�
	 * ��ֵΪ0ʱ���ͽ����¼�����
	 */
	private int flag_viewpager_index;
	/**
	 * ��Ǳ�������ǵ�ǰ��ʾ�Ĳ����Ƿ�Ϊmain_layout,true��ʾ�ǣ�false��ʾ��
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
	 * �Զ��巽������ʼ������
	 */
	private void init() {
		scroller = new Scroller(getContext());
	}
	/**
	 * ��д���������¼����������¼�����ʱ��������
	 * 
	 * �����ͻ1��viewpager���һ����ͱ��ؼ������һ���
	 * ʵ��ԭ��
	 * 1�������������棨main_layout��
	 * 2������viewpager��ʾ��Ԫ��Ϊ��һ��
	 * 3�����Ʒ���Ϊ���һ���
	 * ������������������ʱ�򣬽��������¼����������أ��Ӷ��ﵽ�ؼ����������
	 * 
	 * �����ͻ2����������ʾ��߲��ֵ�ʱ�򣬱����ֿؼ�����ӿؼ��뱾�ؼ������һ�����ͻ
	 * ʵ��ԭ��
	 * 1����ʾ���洦����಼�֣�left_menu��
	 * 2)���һ�������>���»������룬�ж������һ���
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
			//x�����λ�ƣ�>0�����һ���������֮���󻬶�
			float movedx_value = movex-downx;
			//x��y����ľ��룬���ڽ����ͻ2
			float movex_value = Math.abs(movex-downx);
			float movey_value = Math.abs(movey-downy);
			//ͬʱ����3�������������ͻ1
			if(flag_main_layout&&(flag_viewpager_index==0)&&(movedx_value>0f)){
				return true;
			}
			//ͬʱ��������3�������������ͻ2����2�Ƚϣ���Ϊ�˷�ֹ�ն����������õ�����
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
	 * ���������Ӳ��ֲ����ÿ��
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//��ȡ��һ���ӿؼ�
		View left_menu = getChildAt(0);
		//��ȡ�ڶ����ӿؼ�
		View main_layout = getChildAt(1);
		//��λ��һ���ӿؼ�λ��
		left_menu.measure(-left_menu.getLayoutParams().width, heightMeasureSpec);
		//��λ�ڶ����ӿؼ�λ��
		main_layout.measure(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	/**
	 * ָ�������Ӳ��ֵľ���λ��
	 * int l, �ؼ���������Ļ��ߵľ���
	 *  int t, �ؼ���������Ļ�ϱߵľ���
	 *  int r, �ؼ���������Ļ�ұߵľ���
	 *  int b, �ؼ���������Ļ�±ߵľ���
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//ָ����߲���
		getChildAt(0).layout(-getChildAt(0).getLayoutParams().width, 0, 0, b);
		// ָ���ұ߲���
		getChildAt(1).layout(l, t, r, b);
	}
	/**
	 * ��д���Ʒ������������һ���������ʾ��������
	 * ʵ��Ч����
	 * 1���������һ�����Ļ����Ļ������������ʾ�������ؼ�������Ӧ��λ��
	 * 2����Ϊָ�����򣬵����Ҳ��ֻ�����һ���ľ���ʱ�������Զ���ȫ������ȫ��ʾ
	 * 3���Զ���ȫ��ʾ����������ƽ���������д���
	 * ע�⣺scrollBy(��ǰ����+����ֵ=�����Ҫ�ƶ���������λ��)������scrollTo(ֱ���ƶ���������ָ��������λ��)�������Ƶ�����Ļ���ƶ�����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//��¼����ʱ���x��ֵ
			downx = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			//��¼�ƶ�ʱ���xֵ
			Float movex = event.getX();
			//��ȡ�������ƶ�������֮����෴λ�ƣ���Ϊ����scrollby()�������Ƶ�����Ļ�ľ���λ��
			//��������Ҫ��ʾ�ķ���Ч����ʵ�����ƵĻ���λ�Ʒ������෴��
			//��ȡ����֮��ķ���λ��
			int scrollx = (int)(downx-movex);
			//��ȡ�����õ���x������ֵ=֮ǰ��xֵ+��ǰ��x���ƶ���
			int new_scroll_positon = getScrollX()+scrollx;
			//�����ƶ��Ľ��ޣ����ܳ������ҵĲ��ֵı߽�
			if(new_scroll_positon<-getChildAt(0).getLayoutParams().width){
				//����������߽��޴�
				scrollTo(-getChildAt(0).getLayoutParams().width, 0);
				//���������Ľ�������������ģ�0��0����ʾ���򱣳�������ʾ
			}else if(new_scroll_positon>0){
				//����Ļ������ʾ
				scrollTo(0, 0);
				//���һ���������Ļ������Χ���������߽�֮��
			}else{
				//ֱ����ʾ�ƶ��Ľ��
				scrollBy(scrollx, 0);
			}
			//�ؼ��ط�����movexֱ�Ӹ�ֵ��downx��Ŀ�ģ�ʹÿ��scrollx��ֵΪ��ǰmovex����һ��movex֮��ļ�ʱ�ƶ�ֵ
			downx = movex;
			break;
			//������ʾ�����Զ���ȫ���������ƶ�����ʾ�Ŀ�Ȳ�����߲��ֵ�һ��Ŀ�ȵ�ʱ�򣬾ͽ����Զ���ȫ��ʾ����
		case MotionEvent.ACTION_UP:
			//��ȡ��߲��ֵĿ�ȵ�һ���ֵ
			int left_point = -(int)(getChildAt(0).getLayoutParams().width/2.0f);
			//��ǰx������ֵ��-���һ��Ƚ�
			if(getScrollX()<left_point){
				//��ȫ��ʾ����
				updateCurrentState(true);
			}else{
				//�ָ�������ʾ����
				updateCurrentState(false);
			}
			break;

		}
		//ӦΪ����ȫ�Զ���ؼ�����������ֱ����true��ֱ�����ѵ������Ʋ���
		//����Ǽ̳�ĳ��ԭ���Ŀؼ�������Ҫ����super��������ķ���������ĳЩ���ܻᶪʧ
		return true;
	}
	/**
	 * �Զ��巽���������Զ���ʾ��ȫ����
	 * @param flag��trueΪ�Զ���ȫ��falseΪ�ָ�������ʾ
	 * ƽ���ƶ�ʵ��ԭ������scroller���󣬵���startScroll��������������ƽ��������Ȼ��ǰ�ؼ��������invaledite����������
	 * ���пؼ����ػ������������Ҫ��дcomputeScroll()����������ѭ��ʽ��ά��ƽ������
	 */
	private void updateCurrentState(boolean flag) {
		//��ʼ��x�ƶ�����
		int dx = 0;
		if(flag){
			//��ʾ��ߵĲ��֣�����ʹ��ƽ�������������Եú���Ȼ��ֱ����scrollto�������������ԴﵽЧ��������Ч������Ӳ
			dx = -getChildAt(0).getLayoutParams().width-getScrollX();
			//�ı���ʵ���ֱ�Ǳ�����ֵ
			setFlag_main_layout(false);
		}else{
			//������ߵĲ���
			dx = 0-getScrollX();
			//�ı���ʵ���ֱ�Ǳ�����ֵ
			setFlag_main_layout(true);
		}
		//���о���ֵ�������ݻ����ľ����ȡ��ͬ��ʱ�䣬����ƽ���������ٶ�һ����
		int duration = Math.abs(dx*10);
		//����scrollerƽ������
		//		scroller.startScroll(startX(��ʼx����), startY(��ʼy����), dx(x�����ƶ�����), dy��y������ƶ�����, duration������ʱ����);
		scroller.startScroll(getScrollX(), 0, dx, 0, duration);
		//���ÿؼ��ػ淽��
		//�÷����ײ�ԭ��invalidate����--��drawchild����--��computeScroll����
		invalidate();
	}
	/**
	 * ά��ƽ���ƶ��ķ���
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();
		//Ϊtrueʱ��˵��ģ�⶯����û�н�������֮����
		if(scroller.computeScrollOffset()){
			//��scrollerģ���л�ȡ��ǰ��xֵ
			int currentx = scroller.getCurrX();
			//������ָ����λ��
			scrollTo(currentx, 0);
			//���ÿؼ���invalidate��������������ѭ���ػ棬֪��ģ�����������ݽ���
			invalidate();
		}
	}
}
