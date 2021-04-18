package com.xiajun.enums;

/**
 * 视频封面格式
 *
 * @author xiajun
 */
public enum ThumbnailTypeEnum {
    /** gif 格式 */
    GIF("1", ".gif"),
    /** jpeg 格式 */
    JPEG("2", ".jpeg");

    String type;
    String extensionName;

    ThumbnailTypeEnum(String type, String extensionName) {
        this.type = type;
        this.extensionName = extensionName;
    }

    public String getType() {
        return type;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public static String getExtensionNameByType(String type) {
        for (ThumbnailTypeEnum typeEnum : ThumbnailTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum.getExtensionName();
            }
        }
        return null;
    }
}
