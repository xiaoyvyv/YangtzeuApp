package com.yangtzeu.entity;

public class CetCardBean {


    /**
     * ExceuteResultType : 1
     * Message : [{"SID":"8D80C345500739D784519EC6EE7805F3B56C21C6F716A4F660D74789D6A8C79E5278B98AC02D9A44FAE6ADE20F24AE6B176AAEA68D25C42743FD3BD4267AE522","SubjectName":"英语六级笔试","CanPrintTestTicketFlag":1,"Memo":""}]
     * Result : null
     */

    private int ExceuteResultType;
    private String Message;
    private Object Result;

    public int getExceuteResultType() {
        return ExceuteResultType;
    }

    public void setExceuteResultType(int ExceuteResultType) {
        this.ExceuteResultType = ExceuteResultType;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public Object getResult() {
        return Result;
    }

    public void setResult(Object Result) {
        this.Result = Result;
    }

    public static class MessageBean {

        /**
         * SID : 8D80C345500739D784519EC6EE7805F3B56C21C6F716A4F660D74789D6A8C79E5278B98AC02D9A44FAE6ADE20F24AE6B176AAEA68D25C42743FD3BD4267AE522
         * SubjectName : 英语六级笔试
         * CanPrintTestTicketFlag : 1
         * Memo :
         */

        private String SID;
        private String SubjectName;
        private int CanPrintTestTicketFlag;
        private String Memo;

        public String getSID() {
            return SID;
        }

        public void setSID(String SID) {
            this.SID = SID;
        }

        public String getSubjectName() {
            return SubjectName;
        }

        public void setSubjectName(String SubjectName) {
            this.SubjectName = SubjectName;
        }

        public int getCanPrintTestTicketFlag() {
            return CanPrintTestTicketFlag;
        }

        public void setCanPrintTestTicketFlag(int CanPrintTestTicketFlag) {
            this.CanPrintTestTicketFlag = CanPrintTestTicketFlag;
        }

        public String getMemo() {
            return Memo;
        }

        public void setMemo(String Memo) {
            this.Memo = Memo;
        }
    }
}

