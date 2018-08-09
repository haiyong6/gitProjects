package com.ways.app.price.dao;

import com.ways.app.price.model.VersionInfoEntity;
import java.util.List;
import java.util.Map;

/**
 * 型号折扣对比分析DAO层接口
 * @author yinlue
 *
 */
public interface IVersionDiscountRatioDao
{
	
  /**
   * 初始化时间
   * @param paramMap
   * @return
   */
  public List<Map<String, String>> initDate(Map<String, Object> paramMap);

  /**
   * 加载图形和表格
   * @param paramMap
   * @return
   */
  public List<VersionInfoEntity> loadVersionDiscountRatioChartAndTable(Map<String, Object> paramMap);
}
