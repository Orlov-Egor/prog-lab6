package common.interaction;

import java.io.Serializable;

public class Script implements Serializable {
    private String scriptBody;

    public Script(String scriptBody) {
        this.scriptBody = scriptBody;
    }

    public String getScriptBody() {
        return scriptBody;
    }

    @Override
    public String toString() {
        return "Script[" + scriptBody + "]";
    }
}
