package mapper;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import entity.ShareEntity;
import pl.spring.demo.mapper.ShareMapper;
import pl.spring.demo.to.ShareTo;

public class ShareMapperTest {
	
	private ShareEntity firstEntity = new ShareEntity(20130101, "AAA", 12.34);
	private ShareEntity secondEntity = new ShareEntity(20130102, "BBB", 12.34);
	private ShareTo firstTo = new ShareTo(20130101, "AAA", 12.34,1); 
	private ShareTo secondTo = new ShareTo(20130102, "BBB", 12.34,1);
	private List<ShareEntity> entityList = Arrays.asList(firstEntity, secondEntity);
	private List<ShareTo> toList = Arrays.asList(firstTo, secondTo);
	
	@Test
	public void testShouldMapEntityToTo() {
		// given
		ShareTo mapped;
		// when
		mapped=ShareMapper.map(firstEntity);
		// then
		assertEquals(firstTo,mapped);
	}
	
	@Test
	public void testShouldMapToToEntity() {
		// given
		ShareEntity mapped;
		// when
		mapped=ShareMapper.map(firstTo);
		// then
		assertEquals(firstEntity,mapped);
	}
	
	@Test
	public void testShouldMapListEntityToListTo() {
		// given
		List<ShareTo> mapped;
		// when
		mapped=ShareMapper.map2To(entityList);
		// then
		assertEquals(toList,mapped);
	}
	
	@Test
	public void testShouldMapListToToListEntity() {
		// given
		List<ShareEntity> mapped;
		// when
		mapped=ShareMapper.map2Entity(toList);
		// then
		assertEquals(entityList,mapped);
	}


}
