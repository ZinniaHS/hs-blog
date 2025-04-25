package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.constant.MessageConstant;
import com.hs.blog.mapper.BookCategoryMapper;
import com.hs.blog.mapper.BookMapper;
import com.hs.blog.pojo.dto.BookCategoryPageQueryDTO;
import com.hs.blog.pojo.dto.SaveBookCategoryDTO;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.entity.BookCategory;
import com.hs.blog.pojo.vo.BookCategoryVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
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

    @Autowired
    private BookMapper bookMapper;

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

    /**
     * 分页查询图书类型信息
     * @param bookCategoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(BookCategoryPageQueryDTO bookCategoryPageQueryDTO) {
        int pageNum = bookCategoryPageQueryDTO.getPageNum();
        int pageSize = bookCategoryPageQueryDTO.getPageSize();
        Page<BookCategory> pageBook = new Page<>(pageNum, pageSize);
        // 构建查询条件
        QueryWrapper<BookCategory> wrapper = new QueryWrapper<>();
        // 根据创建时间降序排序
        wrapper.orderByDesc("create_time");
        // 模糊查询name
        if (bookCategoryPageQueryDTO.getKeyWord() != null
                && !"".equals(bookCategoryPageQueryDTO.getKeyWord())) {
            wrapper.like("name", bookCategoryPageQueryDTO.getKeyWord());
        }
        // 执行查询
        Page<BookCategory> page = this.page(pageBook, wrapper);
        // 转换成VO类树形结构
        List<BookCategoryVO> records = buildCategoryTree(page.getRecords());
        return new PageResult(page.getTotal(), records);
    }

    /**
     * 新增图书类型
     * @param saveBookCategoryDTO
     * @return
     */
    @Override
    public void saveCategory(SaveBookCategoryDTO saveBookCategoryDTO) {
        BookCategory bookCategory = new BookCategory();
        BeanUtils.copyProperties(saveBookCategoryDTO, bookCategory);
        this.save(bookCategory);
    }

    /**
     * 删除书籍分类
     * @param id,parentId
     * @return
     */
    @Override
    public Result deleteBookCategory(Integer id, Integer parentId) {
        // 首先判断要删除的是一级分类还是二级分类
        if(parentId == null){
            // 如果是一级分类，要删除需满足条件，该一级分类下没有二级分类
            QueryWrapper<BookCategory> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", id);
            if(bookCategoryMapper.selectCount(wrapper) == 0){
                // 没有二级分类，可以删除
                this.removeById(id);
                System.out.println("一级分类下没有二级分类，可以删除");
                return Result.success(MessageConstant.DELETE_BOOK_CATEGORY_SUCCESS);
            }else{
                // 有二级分类，不能删除
                System.out.println("一级分类下有二级分类，不能删除");
                return Result.error(MessageConstant.EXIST_SECOND_CATEGORY);
            }
        }else {
            // 如果是二级分类，需要查询图书表，判断该二级分类下是否有书籍
            QueryWrapper<Book> wrapper = new QueryWrapper<>();
            wrapper.eq("category_id", id);
            if(bookMapper.selectCount(wrapper) == 0){
                // 没有书籍，可以删除
                this.removeById(id);
                return Result.success(MessageConstant.DELETE_BOOK_CATEGORY_SUCCESS);
            }else {
                // 有书籍，不能删除
                return Result.error(MessageConstant.EXIST_BOOKS);
            }
        }


    }
}
