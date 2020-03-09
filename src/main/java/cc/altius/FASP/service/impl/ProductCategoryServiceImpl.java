/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.service.impl;

import cc.altius.FASP.dao.ProductCategoryDao;
import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.DTO.PrgProductCategoryDTO;
import cc.altius.FASP.model.ProductCategory;
import cc.altius.FASP.service.AclService;
import cc.altius.FASP.service.ProductCategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author altius
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    ProductCategoryDao productCategoryDao;
    @Autowired
    private AclService aclService;

    @Override
    public List<PrgProductCategoryDTO> getProductCategoryListForSync(String lastSyncDate) {
        return this.productCategoryDao.getProductCategoryListForSync(lastSyncDate);
    }

    @Override
    @Transactional
    public int saveProductCategoryList(List<ProductCategory> productCategoryList, CustomUserDetails curUser) {
        int rows = 0;
        for (ProductCategory productCategory : productCategoryList) {
            if (productCategory.getProductCategoryId() != 0) {
                // Update 
                if (this.aclService.checkRealmAccessForUser(curUser, productCategory.getRealm().getRealmId())) {
                    rows += this.productCategoryDao.updateProductCategory(productCategory, curUser);
                } else {
                    throw new AccessDeniedException("Access denied");
                }
            } else {
                // Add the row
                if (this.aclService.checkRealmAccessForUser(curUser, productCategory.getRealm().getRealmId())) {
                    rows += this.productCategoryDao.addProductCategory(productCategory, curUser);
                } else {
                    throw new AccessDeniedException("Access denied");
                }
            }
        }
        return rows;
    }

    @Override
    public List<ProductCategory> getProductCategoryList(CustomUserDetails curUser) {
        return this.productCategoryDao.getProductCategoryList(curUser);
    }
    
    @Override
    public List<ProductCategory> getProductCategoryList(CustomUserDetails curUser, int productCategoryId, boolean includeCurrentLevel, boolean includeAllChildren) {
        return this.productCategoryDao.getProductCategoryList(curUser, productCategoryId, includeCurrentLevel, includeAllChildren);
    }

    @Override
    public ProductCategory getProductCategoryById(int productCategoryId, CustomUserDetails curUser) {
        return this.productCategoryDao.getProductCategoryById(productCategoryId, curUser);
    }

}
