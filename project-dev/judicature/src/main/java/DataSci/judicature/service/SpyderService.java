package DataSci.judicature.service;

import java.util.List;

/**
 * 爬虫
 */
public interface SpyderService {
    String spyder(String srt,String end,int max,String tag) throws Exception;
}
