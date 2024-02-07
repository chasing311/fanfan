package com.chasing.fan.controller;

import com.chasing.fan.common.CustomException;
import com.chasing.fan.common.Result;
import com.chasing.fan.common.SessionUtil;
import com.chasing.fan.entity.AddressBook;
import com.chasing.fan.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 获取地址列表
     * @param addressBook
     * @param session
     * @return
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(AddressBook addressBook, HttpSession session) {
        addressBook.setUserId(SessionUtil.getUserId(session));
        List<AddressBook> addressBooks = addressBookService.getListByUserId(addressBook.getUserId());
        return Result.success(addressBooks);
    }

    /**
     * 获取地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null) {
            throw new CustomException("地址信息不存在");
        }
        return Result.success(addressBook);
    }

    /**
     * 添加地址
     * @param addressBook
     * @param session
     * @return
     */
    @PostMapping("/add")
    public Result<AddressBook> addAddress(@RequestBody AddressBook addressBook, HttpSession session) {
        addressBook.setUserId(SessionUtil.getUserId(session));
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }

    /**
     * 修改地址
     * @param addressBook
     * @return
     */
    @PostMapping("edit")
    public Result<String> editAddress(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return Result.success("地址修改成功");
    }

    /**
     * 删除地址
     * @param ids
     * @return
     */
    @DeleteMapping()
    public Result<String> deleteAddress(Long ids) {
        AddressBook addressBook = addressBookService.getById(ids);
        if (addressBook == null) {
            throw new CustomException("地址信息不存在，请刷新重试");
        }
        addressBookService.removeById(ids);
        return Result.success("地址删除成功");
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @param session
     * @return
     */
    @PutMapping("/default")
    public Result<AddressBook> setDefaultAddress(@RequestBody AddressBook addressBook, HttpSession session) {
        addressBook.setUserId(SessionUtil.getUserId(session));
        addressBookService.setDefaultAddress(addressBook);
        return Result.success(addressBook);
    }

    /**
     * 获取默认地址
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> defaultAddress(HttpSession session) {
        AddressBook addressBook = addressBookService.getDefaultAddress(SessionUtil.getUserId(session));
        return Result.success(addressBook);
    }
}