package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.lgvalle.material_animations.DataBinderMapperImpl());
  }
}
