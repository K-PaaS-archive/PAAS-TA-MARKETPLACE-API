package org.openpaas.paasta.marketplace.api.service;

import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.common.ApiConstants;
import org.openpaas.paasta.marketplace.api.common.CommonService;
import org.openpaas.paasta.marketplace.api.domain.Category;
import org.openpaas.paasta.marketplace.api.domain.CategoryList;
import org.openpaas.paasta.marketplace.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
public class CategoryService extends AbstractService {

	@Autowired
	private CommonService commonService;

	@Autowired
    private CategoryRepository categoryRepository;

	/**
	 * 카테고리 목록 조회
	 * 
	 * @return CategoryList
	 */
    public CategoryList getCategoryList() {
        CategoryList categoryList = new CategoryList();
        categoryList.setItems(categoryRepository.findAll());

        return (CategoryList) commonService.setResultModel(categoryList, ApiConstants.RESULT_STATUS_SUCCESS);
    }

    /**
     * 카테고리 상세 조회
     * 
     * @param id
     * @return
     */
    public Category getCategory(Long id) {
        return (Category) commonService.setResultModel(categoryRepository.getOneByIdAndDeleteYn(id, ApiConstants.DELETE_YN_N), ApiConstants.RESULT_STATUS_SUCCESS);
    }

    /**
     * 카테고리 등록
     * 
     * @param category
     * @return
     */
    public Category createCategory(Category category) {
        return (Category) commonService.setResultModel(categoryRepository.save(category), ApiConstants.RESULT_STATUS_SUCCESS);
    }

    /**
     * 카테고리 수정
     * 
     * @param id
     * @param category
     * @return
     */
    public Category updateCategory(Long id, Category category) {
    	Category updCategory = getCategory(id);
    	updCategory.setCategoryName(category.getCategoryName());
    	updCategory.setDeleteYn(category.getDeleteYn());
    	log.info("category: " + updCategory.toString());

    	return (Category) commonService.setResultModel(categoryRepository.save(updCategory), ApiConstants.RESULT_STATUS_SUCCESS);
    }

//    public Category updateCategoryName(Category category) {
//        Assert.notNull(category, "category can't be null.");
//        Assert.notNull(category.getId(), "category id can't be null.");
//
//        Category saved = categoryRepository.findById(category.getId())
//                .orElseThrow(() -> new NotFoundException("saved category can't be null."));
//
//        saved.setCategoryName(category.getCategoryName());
//
//        return categoryRepository.save(saved);
//    }

//    public void deleteCategory(Long id) {
//        categoryRepository.deleteById(id);
//
////        arrangeCategorySeq();
//    }

//    private void swapCategorySeq(Category category, Direction direction) {
//        List<Category> categoryList = categoryRepository.findAll();
//        categoryList.sort((Category c0, Category c1) -> (int) (c0.getSeq() - c1.getSeq()));
//        int index = -1;
//        for (int i = 0; i < categoryList.size(); i++) {
//            Category c = categoryList.get(i);
//            if (c == category) {
//                index = i;
//                break;
//            }
//        }
//        if (index == -1) {
//            throw new RuntimeException("can't find the category's index.");
//        }
//        int targetIndex = -1;
//        if (direction == Direction.Up) {
//            targetIndex = index - 1;
//        }
//        if (direction == Direction.Down) {
//            targetIndex = index + 1;
//        }
//        if (targetIndex < 0 || targetIndex > categoryList.size() - 1) {
//            throw new RuntimeException("can't find the category's target for swap seq. target index: " + targetIndex);
//        }
//
//        Category target = categoryList.get(targetIndex);
//
//        logger.info("swap: {}, {}", category, target);
//
//        Long temp = category.getSeq();
//        category.setSeq(target.getSeq());
//        target.setSeq(temp);
//    }
//
//    private void arrangeCategorySeq() {
//        List<Category> categoryList = categoryRepository.findAll();
//        categoryList.sort((Category c0, Category c1) -> (int) (c0.getSeq() - c1.getSeq()));
//        int i = 0;
//        for (Category category : categoryList) {
//            category.setSeq((long) ++i);
//        }
//    }

}
