package dev.vality.binbase.batch;

import dev.vality.binbase.util.BinBaseDataType;
import lombok.Data;

@Data
public abstract class BinBaseData {
    private String bin;
    private String brand;
    private String bank;
    private String type;
    private String category;
    private String isoname;
    private String isoa2;
    private String isoa3;
    private String isonumber;
    private String url;
    private String phone;

    public abstract BinBaseDataType getDataType();
}
