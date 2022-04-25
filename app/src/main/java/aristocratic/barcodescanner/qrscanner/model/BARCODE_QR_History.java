package aristocratic.barcodescanner.qrscanner.model;

import java.io.Serializable;

public class BARCODE_QR_History implements Serializable {

    private String content;
    private String type;
    private long time;

    public BARCODE_QR_History(String content, String type, long time) {
        this.content = content;
        this.type = type;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
