package pl.spring.demo.dao;

import java.util.List;

import entity.ShareEntity;

public interface StockDao {

    public List<ShareEntity> getStockHistory(Integer date);
    public List<ShareEntity> getSharesAtDate(Integer date);
    
    public int getMinDate();
    public int getMaxDate();
    
}
