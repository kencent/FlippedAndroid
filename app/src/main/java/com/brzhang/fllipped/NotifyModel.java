package com.brzhang.fllipped;

import java.util.List;

public class NotifyModel {
    private final List<MsgBody> msgBody;

    public NotifyModel(List<MsgBody> msgBody) {
        this.msgBody = msgBody;
    }

    public List<MsgBody> getMsgBody() {
        return msgBody;
    }

    public static class MsgBody {
        private final String msgType;

        private final MsgContent msgContent;

        public MsgBody(String msgType, MsgContent msgContent) {
            this.msgType = msgType;
            this.msgContent = msgContent;
        }

        public String getMsgType() {
            return msgType;
        }

        public MsgContent getMsgContent() {
            return msgContent;
        }

        public static class MsgContent {
            private final String desc;

            private final String ext;

            public MsgContent(String desc, String ext) {
                this.desc = desc;
                this.ext = ext;
            }

            public String getDesc() {
                return desc;
            }

            public String getExt() {
                return ext;
            }
        }
    }
}
