package com.gq.mylib.netWork;

import android.text.TextUtils;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by gaoqun on 2016/7/19.
 */
public class NetWork {
    private static Retrofit mRetrofit;

    private NetWork(Retrofit retrofit) {
        mRetrofit = retrofit;
    }

    public static <T> T createService(Class<T> service) throws NullPointerException{
        return mRetrofit.create(service);
    }

    public static final class Builder {
        private OkHttpClient mOkHttpClient;
        private HttpLoggingInterceptor mLogger;
        private Retrofit sRetrofit;
        private Interceptor mInterceptor;
        private String mUrl;
        private Converter.Factory mConvertFactory;
        private CallAdapter.Factory mCallAdapterFactory;

        public Builder setOkhttpLogInterceptor(HttpLoggingInterceptor okhttpLogInterceptor) {
            mLogger = okhttpLogInterceptor;
            return this;
        }

        public Builder setInterceptor(Interceptor interceptor) {
            this.mInterceptor = interceptor;
            return this;
        }

        public Builder setOkhttp(OkHttpClient okHttpClient) {
            mOkHttpClient = okHttpClient;
            return this;
        }

        public Builder setRetrofit(Retrofit retrofit) {
            sRetrofit = retrofit;
            return this;
        }

        public Builder setBaseUrl(String url) {
            if (TextUtils.isEmpty(url)) {
                throw new IllegalArgumentException("url can't be null Or '_'!");
            } else
                mUrl = url;
            return this;
        }

        public Builder addConverterFactory(Converter.Factory factory) {
            this.mConvertFactory = factory;
            return this;
        }

        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            this.mCallAdapterFactory = factory;
            return this;
        }

        public NetWork build() {
            if (mOkHttpClient == null) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                if (mInterceptor != null) {
                    builder.addInterceptor(mInterceptor);
                }
                if (mLogger != null) {
                    builder.addInterceptor(mLogger);
                }
            }

            if (this.sRetrofit == null) {
                Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
                if (!TextUtils.isEmpty(mUrl)) {
                    retrofitBuilder.baseUrl(mUrl);
                }
                if (mOkHttpClient != null) {
                    retrofitBuilder.client(mOkHttpClient);
                }
                if (mConvertFactory != null) {
                    retrofitBuilder.addConverterFactory(mConvertFactory);
                }
                if (mCallAdapterFactory != null) {
                    retrofitBuilder.addCallAdapterFactory(mCallAdapterFactory);
                }
                sRetrofit = retrofitBuilder.build();
            }
            return new NetWork(sRetrofit);
        }
    }
}
