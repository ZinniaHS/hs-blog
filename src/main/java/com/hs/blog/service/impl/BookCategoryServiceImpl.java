package com.hs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.mapper.BookCategoryMapper;
import com.hs.blog.pojo.dto.SelectedCategoryBooksDTO;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.entity.BookCategory;
import com.hs.blog.pojo.vo.BookCategoryVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.service.IBookCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookCategoryServiceImpl
        extends ServiceImpl<BookCategoryMapper, BookCategory> implements IBookCategoryService {

    @Autowired
    private BookCategoryMapper bookCategoryMapper;

    @Override
    public List<BookCategoryVO> getBookCategory() {
        // 查询所有分类
        List<BookCategory> categoryList = bookCategoryMapper.selectList(null);

        // 转换并构建树形结构
        return buildCategoryTree(categoryList);
    }



    private List<BookCategoryVO> buildCategoryTree(List<BookCategory> categories) {
        // 创建VO列表并拷贝属性
        List<BookCategoryVO> voList = new ArrayList<>();
        categories.forEach(category -> {
            BookCategoryVO vo = new BookCategoryVO();
            BeanUtils.copyProperties(category, vo);
            voList.add(vo);
        });

        // 使用Map存储节点，便于快速查找
        Map<Integer, BookCategoryVO> nodeMap = new HashMap<>();
        List<BookCategoryVO> rootNodes = new ArrayList<>();

        // 第一次遍历：建立节点索引
        voList.forEach(vo -> {
            nodeMap.put(vo.getId(), vo);
            if (vo.getParentId() == null || vo.getParentId() == 0) {
                rootNodes.add(vo);
            }
        });

        // 第二次遍历：构建树形结构
        voList.forEach(vo -> {
            if (vo.getParentId() != null && vo.getParentId() != 0) {
                BookCategoryVO parentNode = nodeMap.get(vo.getParentId());
                if (parentNode != null) {
                    if (parentNode.getChildren() == null) {
                        parentNode.setChildren(new ArrayList<>());
                    }
                    parentNode.getChildren().add(vo);
                }
            }
        });

        return rootNodes;
    }
}
