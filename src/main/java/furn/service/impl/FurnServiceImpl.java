package furn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import furn.bean.Furn;
import furn.mapper.FurnMapper;
import furn.service.FurnService;
import org.springframework.stereotype.Service;

@Service
public class FurnServiceImpl extends ServiceImpl<FurnMapper, Furn> implements FurnService {
}
