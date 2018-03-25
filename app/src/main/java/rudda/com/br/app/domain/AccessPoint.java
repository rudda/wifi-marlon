package rudda.com.br.app.domain;

/**
 * Created by Rudda Beltrao on 25/03/2018.
 */

public class AccessPoint {

    private String SSID;
    private String preSharedKey;
    private String BSSID;

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getPreSharedKey() {
        return preSharedKey;
    }

    public void setPreSharedKey(String preSharedKey) {
        this.preSharedKey = preSharedKey;
    }

}

