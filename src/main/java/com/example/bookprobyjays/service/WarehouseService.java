package com.example.bookprobyjays.service;

import com.example.bookprobyjays.domain.Warehouse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author chenjiexiang
* @description 针对表【warehouse(存储书籍库存信息的表)】的数据库操作Service
* @createDate 2023-12-12 09:24:51
*/
public interface WarehouseService extends IService<Warehouse> {
    //增加仓库书籍信息
    Warehouse addWarehouseBook(Warehouse warehouse);
    //删除仓库书籍信息
    Long deleteWarehouseBook(Long id);
    //修改仓库书籍信息
    Warehouse updateWarehouseBook(Warehouse warehouse);
    //查看仓库库存，并显示书籍相关信息(可能需要一个vo)

}