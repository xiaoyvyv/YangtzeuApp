package com.lib.mob.im;

import com.blankj.utilcode.util.SPUtils;
import com.mob.MobSDK;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.model.IMUser;
import com.mob.tools.utils.ResHelper;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.utils.MyUtils;

import java.io.File;

public class IMManager {
	private static IMUser user = null;

	public static IMUser getUser() {
		if (user == null) {
			user = getCachedUser();
		}
		return user;
	}

	public static void getIMUser(final OnResultListener<IMUser> listener) {
		MobIM.getCurrentIMUser(new MobIMCallback<IMUser>() {
			@Override
			public void onSuccess(IMUser user) {
				if (listener != null) {
					listener.onResult(user);
				}
			}

			@Override
			public void onError(int i, String s) {
				if (listener != null) {
					listener.onResult(getCachedUser());
				}
			}
		});
	}



	private static IMUser getCachedUser() {
		//姓名
		String number = SPUtils.getInstance("user_info").getString("number", "000000");
		String name = SPUtils.getInstance("user_info").getString("name", "用户：" + number);
		String qq = SPUtils.getInstance("user_info").getString("qq", "2440888027");

		MobSDK.setUser(number, name, MyUtils.getQQHeader(qq), null);
		IMUser user = new IMUser();
		user.setId(number);
		user.setNickname(name);
		user.setAvatar(MyUtils.getQQHeader(qq));
		return user;
	}

	public static void loginIM() {
		String number = SPUtils.getInstance("user_info").getString("number", "000000");
		String name = SPUtils.getInstance("user_info").getString("number", "用户：" + number);
		String qq = SPUtils.getInstance("user_info").getString("qq", "2440888027");
		MobSDK.setUser(number, name, MyUtils.getQQHeader(qq), null);
	}
}
