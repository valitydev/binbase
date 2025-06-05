package dev.vality.binbase.batch;

import dev.vality.binbase.util.BinBaseDataType;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = "record")
public class BinBaseXmlData extends BinBaseData {
    @Override
    public BinBaseDataType getDataType() {
        return BinBaseDataType.XML;
    }
}
