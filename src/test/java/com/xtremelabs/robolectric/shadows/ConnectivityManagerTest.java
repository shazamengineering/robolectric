package com.xtremelabs.robolectric.shadows;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.WithTestDefaultsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

@RunWith(WithTestDefaultsRunner.class)
public class ConnectivityManagerTest {
    private ConnectivityManager connectivityManager;
    private ShadowNetworkInfo shadowOfActiveNetworkInfo;
    private ShadowConnectivityManager shadowConnectivityManager;

    @Before
    public void setUp() throws Exception {
        connectivityManager = (ConnectivityManager) Robolectric.application.getSystemService(Context.CONNECTIVITY_SERVICE);
        shadowConnectivityManager = shadowOf(connectivityManager);
        shadowOfActiveNetworkInfo = shadowOf(connectivityManager.getActiveNetworkInfo());
    }

    @Test
    public void getConnectivityManagerShouldNotBeNull() {
        assertNotNull(connectivityManager);
        assertNotNull(connectivityManager.getActiveNetworkInfo());
    }

    @Test
    public void networkInfoShouldReturnTrueCorrectly() {
        shadowOfActiveNetworkInfo.setConnectionStatus(true);

        assertTrue(connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting());
        assertTrue(connectivityManager.getActiveNetworkInfo().isConnected());
    }

    @Test
    public void getNetworkInfoShouldReturnAssignedValue() throws Exception {
        NetworkInfo networkInfo = ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTING);
        shadowConnectivityManager.setNetworkInfo(ConnectivityManager.TYPE_WIFI, networkInfo);
        NetworkInfo actual = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        assertThat(actual, sameInstance(networkInfo));
        assertThat(actual.getDetailedState(), is(NetworkInfo.DetailedState.CONNECTING));
    }

    @Test
    public void networkInfoShouldReturnFalseCorrectly() {
        shadowOfActiveNetworkInfo.setConnectionStatus(false);

        assertFalse(connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting());
        assertFalse(connectivityManager.getActiveNetworkInfo().isConnected());
    }
    
    @Test
    public void networkInfoShouldReturnTypeCorrectly(){
    	shadowOfActiveNetworkInfo.setConnectionType(ConnectivityManager.TYPE_MOBILE);
    	assertEquals(ConnectivityManager.TYPE_MOBILE, shadowOfActiveNetworkInfo.getType());
    	
    	shadowOfActiveNetworkInfo.setConnectionType(ConnectivityManager.TYPE_WIFI);
    	assertEquals(ConnectivityManager.TYPE_WIFI, shadowOfActiveNetworkInfo.getType());
    }
}
