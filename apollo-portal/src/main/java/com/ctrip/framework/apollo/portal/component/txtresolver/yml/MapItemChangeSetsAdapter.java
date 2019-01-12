package com.ctrip.framework.apollo.portal.component.txtresolver.yml;

import com.ctrip.framework.apollo.common.dto.ItemChangeSets;
import com.ctrip.framework.apollo.common.dto.ItemDTO;
import com.ctrip.framework.apollo.common.utils.BeanUtils;
import com.google.common.base.Joiner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kezhenxu at 2019/1/12 16:25
 *
 * @author kezhenxu (kezhenxu94 at 163 dot com)
 */
public class MapItemChangeSetsAdapter extends ItemChangeSets {
  @SuppressWarnings("unchecked")
  public MapItemChangeSetsAdapter(
      final String prefix,
      final Map<String, Object> map,
      final List<ItemDTO> baseItems) {
    final Map<String, ItemDTO> oldItemsByKey = BeanUtils.mapByKey("key", baseItems);
    final Set<String> oldKeys = oldItemsByKey.keySet();

    for (final Map.Entry<String, Object> entry : map.entrySet()) {
      final String key = prefix + entry.getKey();
      final Object value = entry.getValue();
      if (value instanceof Map) {
        final MapItemChangeSetsAdapter nestedObject =
            new MapItemChangeSetsAdapter(key + ".", ((Map<String, Object>) value), baseItems);
        for (final ItemDTO createItem : nestedObject.getCreateItems()) {
          addCreateItem(createItem);
        }
        for (final ItemDTO updateItem : nestedObject.getUpdateItems()) {
          addUpdateItem(updateItem);
        }
        for (final ItemDTO deleteItem : nestedObject.getDeleteItems()) {
          addDeleteItem(deleteItem);
        }
      } else if (value instanceof List) {
        final List<?> arrayValues = (List<?>) value;
        final String stringValue = Joiner.on(",").join(arrayValues);
        final ItemDTO item = oldItemsByKey.getOrDefault(key, new ItemDTO(key, stringValue, "", 0));
        if (item.getId() > 0) {
          addUpdateItem(item);
        } else {
          addCreateItem(item);
        }
        for (final String oldKey : oldKeys) {
        }
      }
    }
  }
}
