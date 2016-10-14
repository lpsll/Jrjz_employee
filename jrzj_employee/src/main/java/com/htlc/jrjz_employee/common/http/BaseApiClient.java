package com.htlc.jrjz_employee.common.http;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.htlc.jrjz_employee.AppConfig;
import com.htlc.jrjz_employee.common.utils.LogUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class BaseApiClient {

	public static final Gson gson = new Gson();
	private static final OkHttpClient mOkHttpClient = OkHttpUtils.getInstance().getOkHttpClient();
	public static final MediaType M_JSON = MediaType
			.parse("application/json; charset=utf-8");

	public static <T> void get(String url,
							   AsyncCallBack<T> asyncCallBack) {

		LogUtils.e("get-------------no reqParams-------------");
		Request request = new Request.Builder().tag(asyncCallBack.getTag())
				.url(url).get().build();
		enqueue(request, asyncCallBack);
	}


	public static <T> void get(String url,Object dto,
							   AsyncCallBack<T> asyncCallBack) {
		LogUtils.e("http_request_url:" + url);

		if(dto!=null){
			LogUtils.e("get-------------reqParams   start-------------");
			Map<String, ?> map = objectToMap(dto);
			if (map == null)
				return;
			Set<String> key = map.keySet();
			String params="";
			String beginLetter="?";
			for (Iterator<String> it = key.iterator(); it.hasNext();) {
				String s =  it.next();
//				if(TextUtils.isEmpty(map.get(s).toString())){
//					LogUtils.e("Found Empty Params--> "+s + "=" + map.get(s));
//					continue;
//				}
				if (params.equals(""))
				{
//					params += beginLetter + s + "=" + map.get(s);
					params += map.get(s);
				}
				else
				{
					params += "&" + s + "=" + map.get(s);
				}

			}
			LogUtils.e("get-------------reqParams    end-------------");
			url+=params;

		}
		LogUtils.e("get url:",""+url);
		Request request = new Request.Builder().tag(asyncCallBack.getTag())
				.url(url).get().build();
		enqueue(request, asyncCallBack);
	}


	public static void postString(){

	}

	protected void appendHeaders(Request.Builder builder,Map<String, String> headers)
	{
		Headers.Builder headerBuilder = new Headers.Builder();
		if (headers == null || headers.isEmpty()) return;

		for (String key : headers.keySet())
		{
			headerBuilder.add(key, headers.get(key));
		}
		builder.headers(headerBuilder.build());
	}


	/**
	 * 直接传json
	 * @param url
	 * @param dto 请求体中封装data的对象模型
	 * @param asyncCallBack
	 * @param <T>
	 */
	public static <T> void postString(String url,Object dto,
								AsyncCallBack<T> asyncCallBack) {
		String data = gson.toJson(dto);
		LogUtils.i(data);
		RequestBody body = RequestBody.create(M_JSON, data);
		Request request = new Request.Builder()
				.tag(asyncCallBack.getTag())
				.header("Content-Type","application/json")
				.url(url).post(body).build();
		enqueue(request, asyncCallBack);
	}
	/**
	 * post传键值对
	 */
	public static <T> void post(String url, Object dto,
								AsyncCallBack<T> asyncCallBack) {
		LogUtils.e("http_request_url:" + url);
		FormBody.Builder builder = new FormBody.Builder();
		LogUtils.e("post-------------reqParams   start-------------");
		Map<String, ?> map = objectToMap(dto);
		if (map == null)
			return;
		Set<String> key = map.keySet();
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			String s =  it.next();
//			if(TextUtils.isEmpty(map.get(s).toString())){
//				LogUtils.e("Found Empty Params--> "+s + "=" + map.get(s));
//				continue;
//			}
			builder.add(s, map.get(s).toString());
			LogUtils.e(s + " = " + map.get(s).toString());
		}
		LogUtils.e("post-------------reqParams    end-------------");

		LogUtils.e("builder.build()--",""+builder.build());
		Request request = new Request.Builder()
				.tag(asyncCallBack.getTag())
				.url(url)
				.post(builder.build())
				.build();
		enqueue(request, asyncCallBack);
	}

	public static <T> void enqueue( Request request,
								   AsyncCallBack<T> asyncCallBack) {
		mOkHttpClient.newCall(request).enqueue(asyncCallBack);
	}

	/**
	 * 根据tag取消网络请求
	 *
	 * @param tag
	 *            网络请求标记
	 */
	public static void cancelCall(Object tag) {
		OkHttpUtils.getInstance().cancelTag(tag);
	}

	/**
	 * 获取服务器绝对路径
	 *
	 * @param relativeUrl 相对路径
	 * @return 返回绝对路径地址
	 */
	public static String getAbsoluteUrl(String relativeUrl) {
		return AppConfig.BASE_URL+relativeUrl;
	}


	public static Map<String, Object> objectToMap(Object o) {
		if (o == null) {
			return null;
		}
		return JSON.parseObject(gson.toJson(o), HashMap.class);
	}

}
