package com.ua.notifier;

/**
 * Класс для зберігання параметрів (паролі, шлюзи, ....) конкретного типу повідомлення (пошта, смс, вайбер...)
 * Використовується  в хешмапі властивостей повідомлень Main.props
 */
public class ConfigItem {

    private Integer notifierTpId;
    private Boolean enabled;
    private Integer retryCount;
    private String gateHost;
    private Integer gatePort;
    private String gateLogin;
    private String gatePasswd;

    public ConfigItem(Integer notifierTpId, Boolean enabled, Integer retryCount,String gateHost,Integer gatePort, String gateLogin, String gatePasswd){
         super();
         setNotifierTpId(notifierTpId);
         setEnabled(enabled);
         setRetryCount(retryCount);
         setGateHost(gateHost);
         setGatePort(gatePort);
         setGateLogin(gateLogin);
         setGatePasswd(gatePasswd);
    }


    public Integer getNotifierTpId() {
        return notifierTpId;
    }

    public void setNotifierTpId(Integer notifierTpId) {
        this.notifierTpId = notifierTpId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getGateHost() {
        return gateHost;
    }

    public void setGateHost(String gateHost) {
        this.gateHost = gateHost;
    }

    public Integer getGatePort() {
        return gatePort;
    }

    public void setGatePort(Integer gatePort) {
        this.gatePort = gatePort;
    }

    public String getGateLogin() {
        return gateLogin;
    }

    public void setGateLogin(String gateLogin) {
        this.gateLogin = gateLogin;
    }

    public String getGatePasswd() {
        return gatePasswd;
    }

    public void setGatePasswd(String gatePasswd) {
        this.gatePasswd = gatePasswd;
    }
}
