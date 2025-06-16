package com.regain.product.service.post;

import com.regain.product.repository.IPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostPackageServiceImpl implements IPostPackageService {

    @Autowired
    private IPostRepository iPostRepository;
}
