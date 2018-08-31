#  基于exoplayer的自定义播放器 #
## 效果图##
![效果图](http://ohdryj9ow.bkt.clouddn.com/player.gif)

[http://ohdryj9ow.bkt.clouddn.com/player.gif](http://ohdryj9ow.bkt.clouddn.com/player.gif "查看不了，请点击")

## 使用方法 ##
1.在需要播放视频的页面的xml布局添加以下控件

    <com.acode.player.lib.AcodePlayerView
        android:id="@+id/acodePlayerView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

    </com.acode.player.lib.AcodePlayerView>

2.在activity或者frgament初始化
 
	private AcodePlayerView acodePlayerView;
	acodePlayerView = findViewById(R.id.acodePlayerView);

3.设置数据源进行播放

	 acodePlayerView.readyPlayer(playerBean);


4.必须重写以下方法


    @Override
    protected void onResume() {
		Log.d(TAG, "再次回到本页面");
        super.onResume();
        acodePlayerView.onResume();
    }

    @Override
    protected void onPause() {
		Log.d(TAG, "页面切换");
        super.onPause();
        acodePlayerView.onPause();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "横竖屏切换");
        super.onConfigurationChanged(newConfig);
        acodePlayerView.onConfigurationChanged(newConfig.orientation);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "销毁");
		super.onDestroy();
        if (acodePlayerView != null) {
            acodePlayerView.cancel();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (acodePlayerView == null) {
            return super.onKeyDown(keyCode, event);
        }
        return acodePlayerView.onKeyDown(keyCode, event);
    }

## 1.0.0版本 ##
1. 实现基本播放功能
2. 横竖屏切换保存当前播放状态
3. 页面切换保存当前播放状态
4. 切换至后台保存当前播放状态 
5. 手势控制亮度，进度，音量等操作
6. 网络状况不好的情况下的友好提示

## 1.0.1版本 ##
1. 修改手势滑动冲突的缺陷
## 1.0.2版本 ##
1. 增加多线路播放(超清，高清，标清)
## 1.0.3版本 ##
1. 增加列表播放

