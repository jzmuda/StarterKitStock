package pl.spring.demo.dao.impl;

import pl.spring.demo.dao.StockDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import entity.ShareEntity;


public class StockDaoImpl implements StockDao{
	
	private List<ShareEntity> data;
	
	public StockDaoImpl() {
		data = new ArrayList<ShareEntity>();		
	}
	
	public StockDaoImpl(String fileName) {
		data = new ArrayList<ShareEntity>();
		load(fileName);
	}

	public void load(String fileName) {
		Path currentRelativePath = Paths.get("");
		String openFileName = currentRelativePath.toAbsolutePath().toString();
		openFileName+="\\src\\main\\resources\\"+fileName;
		File file = new File(openFileName);
		
		Scanner input;
		try {
			input = new Scanner(file);
			while (input.hasNextLine()) {
				String line = input.nextLine();
				data.add(parseEntity(line));
			}
			input.close();
		} catch (FileNotFoundException e) {
			
			throw new IllegalArgumentException("File "+openFileName+" not found");
		}
		Collections.sort(data);
	}
		
	private ShareEntity parseEntity(String line) {
		String share[]=line.split(",");
		if (share.length!=3)
			throw new IllegalArgumentException("Corrupt Data File: bad record length");
		try{
			Integer.parseInt(share[1]);
			Double.parseDouble(share[2]);
		} catch(Exception e) {
			throw new IllegalArgumentException("Corrupt Data File: date or value not a number");
		}
		return new ShareEntity(Integer.parseInt(share[1]), share[0], Double.parseDouble(share[2]));
	}

	public List<ShareEntity> getStockHistory(Integer currentDate) {
		List<ShareEntity> result = new ArrayList<ShareEntity>();
		for(ShareEntity share:data) {
			if(share.getDate()<=currentDate) {
				result.add(share);
			}
			else break;
		}
		return result;
	}

	@Override
	public int getMinDate() {
		return data.get(0).getDate();
	}

	@Override
	public int getMaxDate() {
		return data.get(data.size()-1).getDate();
	}

	@Override
	public List<ShareEntity> getSharesAtDate(Integer date) {
		List<ShareEntity> result = new ArrayList<ShareEntity>();
		for(ShareEntity share:data) {
			if(share.getDate().equals(date)) {
				result.add(share);
			}
		}
		return result;
	}
	
	   
}
