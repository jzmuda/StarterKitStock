package pl.spring.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import entity.ShareEntity;
import pl.spring.demo.to.ShareTo;

public class ShareMapper {
	public static ShareTo map(ShareEntity shareEntity) {
		if (shareEntity != null) {
			return new ShareTo(shareEntity.getDate(), shareEntity.getCompany(), shareEntity.getValue(),
					1);
		}
		return null;
	}

	public static ShareEntity map(ShareTo shareTo) {
		if (shareTo != null) {
			return new ShareEntity(shareTo.getDate(), shareTo.getCompany(), shareTo.getValue());
		}
		return null;
	}

	public static List<ShareTo> map2To(List<ShareEntity> shareEntities) {
		return shareEntities.stream().map(ShareMapper::map).collect(Collectors.toList());
	}

	public static List<ShareEntity> map2Entity(List<ShareTo> shareTos) {
		return shareTos.stream().map(ShareMapper::map).collect(Collectors.toList());
	}

}
