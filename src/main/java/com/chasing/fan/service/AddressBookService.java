package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.AddressBook;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {
    List<AddressBook> getListByUserId(Long userId);
    void setDefaultAddress(AddressBook addressBook);
    AddressBook getDefaultAddress(Long userId);
}
