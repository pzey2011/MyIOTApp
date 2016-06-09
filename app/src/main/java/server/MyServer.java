package server;

/**
 * Created by asus on 6/7/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.asus.myapp.LoginActivity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MyServer extends NanoHTTPD {
    private final static int PORT = 8080;
    private Context context;
    private SharedPreferences sharedPreferences;
    public MyServer(Context context) throws IOException {
        super(PORT);
        this.context=context;
        start();
        System.out.println( "\nRunning! Point your browsers to http://" +
                getWifiApIpAddress() + ":8080/ \n" );
    }

    @Override
    public Response serve(IHTTPSession session) {


        sharedPreferences= context.getSharedPreferences(LoginActivity.MyPREFERENCES,Context.MODE_PRIVATE);
        String  msg1=sharedPreferences.getString("SSID","ali");
        msg1+=";";
        msg1+=sharedPreferences.getString("Password","ali");
        msg1+=";";

        msg1+=sharedPreferences.getString("IP","ali");
        msg1+=";";
        return newFixedLengthResponse( msg1  );
    }
    public String getWifiApIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                            .hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            Log.d("mTag", inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("mTag", ex.toString());
        }
        return null;
    }
}