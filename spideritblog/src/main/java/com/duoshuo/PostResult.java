package com.duoshuo;


/**
 * Created by Administrator on 2014/12/28.
 */
public class PostResult {

    Cursor cursor;
    Response[] response;

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public Response[] getResponse() {
        return response;
    }

    public void setResponse(Response[] response) {
        this.response = response;
    }

    static class Cursor{
        int pages;
        int total;

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    static class Response{
        String thread_id;
        String title;
        String thread_key;
        String url;

        public String getThread_id() {
            return thread_id;
        }

        public void setThread_id(String thread_id) {
            this.thread_id = thread_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getThread_key() {
            return thread_key;
        }

        public void setThread_key(String thread_key) {
            this.thread_key = thread_key;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
