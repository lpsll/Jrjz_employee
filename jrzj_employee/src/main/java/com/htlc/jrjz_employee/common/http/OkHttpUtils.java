package com.htlc.jrjz_employee.common.http;

import android.view.View;

import com.htlc.jrjz_employee.common.http.cookie.CookieJarImpl;
import com.htlc.jrjz_employee.common.http.cookie.store.CookieStore;
import com.htlc.jrjz_employee.common.http.cookie.store.HasCookieStore;
import com.htlc.jrjz_employee.common.http.cookie.store.MemoryCookieStore;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Call;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

/**
 * Created by John_Libo on 2016/8/15.
 */
public class OkHttpUtils {

    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;

    public OkHttpUtils(OkHttpClient okHttpClient)
    {
        if (okHttpClient == null)
        {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            //cookie enabled
            okHttpClientBuilder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier()
            {
                @Override
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            });

            mOkHttpClient = okHttpClientBuilder.build();
            mOkHttpClient.newBuilder()
                    .connectTimeout(30, TimeUnit.MILLISECONDS)
                    .readTimeout(30, TimeUnit.MILLISECONDS)
                    .writeTimeout(10, TimeUnit.MILLISECONDS);
        } else
        {
            mOkHttpClient = okHttpClient;
        }

    }





    public static OkHttpUtils getInstance(OkHttpClient okHttpClient)
    {
        if (mInstance == null)
        {
            synchronized (OkHttpUtils.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance()
    {
        if (mInstance == null)
        {
            synchronized (OkHttpUtils.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkHttpUtils(null);
                }
            }
        }
        return mInstance;
    }



    public OkHttpClient getOkHttpClient()
    {
        return mOkHttpClient;
    }



    public CookieStore getCookieStore()
    {
        final CookieJar cookieJar = mOkHttpClient.cookieJar();
        if (cookieJar == null)
        {
            throw new IllegalArgumentException("you should invoked okHttpClientBuilder.cookieJar() to set a cookieJar.");
        }
        if (cookieJar instanceof HasCookieStore)
        {
            return ((HasCookieStore) cookieJar).getCookieStore();
        } else
        {
            return null;
        }
    }



    public void cancelTag(Object tag)
    {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
    }


    /**
     * for https-way authentication
     *
     * @param certificates
     */
    public void setCertificates(InputStream... certificates)
    {
        SSLSocketFactory sslSocketFactory = HttpsUtils.getSslSocketFactory(certificates, null, null);

        OkHttpClient.Builder builder = getOkHttpClient().newBuilder();
        builder = builder.sslSocketFactory(sslSocketFactory);
        mOkHttpClient = builder.build();


    }

    /**
     * for https mutual authentication
     *
     * @param certificates
     * @param bksFile
     * @param password
     */
    public void setCertificates(InputStream[] certificates, InputStream bksFile, String password)
    {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, bksFile, password))
                .build();
    }

    public void setHostNameVerifier(HostnameVerifier hostNameVerifier)
    {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .hostnameVerifier(hostNameVerifier)
                .build();
    }

    public void clearSession(View view)
    {
        OkHttpUtils.getInstance().getCookieStore().removeAll();
    }

}
