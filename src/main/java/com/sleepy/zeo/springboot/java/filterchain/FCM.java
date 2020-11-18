package com.sleepy.zeo.springboot.java.filterchain;

import java.util.ArrayList;
import java.util.List;

public class FCM {

    public interface Filter {
        void doFilter(String req, String res, FilterChain chain);
    }

    public static class FaceFilter implements Filter {

        @Override
        public void doFilter(String req, String res, FilterChain chain) {
            System.out.println("FaceFilter doFilter");
            chain.doFilter(req, res);
        }
    }

    public static class HTMLFilter implements Filter {

        @Override
        public void doFilter(String req, String res, FilterChain chain) {
            System.out.println("HTMLFilter doFilter");
            chain.doFilter(req, res);
        }
    }

    public static class SensitiveFilter implements Filter {

        @Override
        public void doFilter(String req, String res, FilterChain chain) {
            System.out.println("SensitiveFilter doFilter");
            chain.doFilter(req, res);
        }
    }

    public static class FilterChain {
        List<Filter> filters = new ArrayList<>();
        int pos = 0;
        int count = 0;

        FilterChain addFilter(Filter f) {
            this.filters.add(f);
            count++;
            return this;
        }

        void doFilter(String req, String res) {
            if (this.pos < this.count) {
                filters.get(this.pos++).doFilter(req, res, this);
            }
        }
    }

    public static void main(String[] args) {
        FilterChain chain = new FilterChain();
        chain.addFilter(new FaceFilter())
                .addFilter(new HTMLFilter())
                .addFilter(new SensitiveFilter());

        System.out.println("FilterChain doFilter begins");
        chain.doFilter("request", "response");
        System.out.println("FilterChain doFilter ends");
    }
}
