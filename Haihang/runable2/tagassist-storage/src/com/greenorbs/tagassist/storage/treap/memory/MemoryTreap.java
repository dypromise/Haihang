package com.greenorbs.tagassist.storage.treap.memory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.greenorbs.tagassist.storage.treap.ITreap;



/*
 * Treap�еĽڵ�
 */
class TreapNode<K extends Comparable<K>,V>
{
	K key;
	V value;
	int fix=0;
	int r_size=0;
	int l_size=0;
	int rNo=-1;
	int lNo=-1;
}

/**
 * Treap ���ݽṹ
 * @author Administrator
 *
 * @param <K> ������Ҫ���ԱȽϴ�С
 * @param <V> ֵ
 */
public class MemoryTreap<K extends Comparable<K>,V> implements ITreap<K, V>  {
	
	int size;
	int rootNo;
	TreapNode<K,V>[] nodeAry;
	
	//Ĭ�ϵ�����
	private static final int DEFAULT_CAPABILITY = 1000;
	
	@SuppressWarnings("unchecked")
	public MemoryTreap(int  capability){
		this.nodeAry = new TreapNode[capability];
		this.size = 0 ;
		this.rootNo = -1;
	}
	
	public MemoryTreap(){this(DEFAULT_CAPABILITY);};
	
	//д��
	/* (non-Javadoc)
	 * @see fx.sunjoy.algo.impl.ITreap#put(K, V)
	 */
	@Override
	public void put(K key,V value){
		this.rootNo = insert(this.rootNo,key,value);
	}
	
	//����
	/* (non-Javadoc)
	 * @see fx.sunjoy.algo.impl.ITreap#get(K)
	 */
	@Override
	public V get(K key){
		int idx =  find(this.rootNo,key);
		if(idx==-1)return null;
		else return this.nodeAry[idx].value;
	}
	
	//��Χ��ѯ
	/* (non-Javadoc)
	 * @see fx.sunjoy.algo.impl.ITreap#range(K, K)
	 */
	@Override
	public Map<K,V> range(K start,K end,int limit)
	{
		Map<K,V> result = new TreeMap<K, V>();
		collectRange(this.rootNo, start, end, result);
		return result;
	}
	
	@Override
	public Map<K,V> prefix(K prefixString,int limit,K startK,boolean asc){
		Map<K,V> results = new TreeMap<K,V>();
		prefixSearch(this.rootNo,prefixString,results);
		return results;
	}
	
	

	//����
	/* (non-Javadoc)
	 * @see fx.sunjoy.algo.impl.ITreap#length()
	 */
	@Override
	public int length(){
		return this.nodeAry[this.rootNo].l_size+1+this.nodeAry[this.rootNo].r_size;
	}
	
	//ɾ��
	/* (non-Javadoc)
	 * @see fx.sunjoy.algo.impl.ITreap#delete(K)
	 */
	@Override
	public boolean delete(K key){
		remove(this.rootNo,key);
		return true;
	}
	
	
	private void prefixSearch(int startNode, K prefixString,Map<K,V> results) {
		if(startNode==-1)return ;
		TreapNode<K, V> cur = this.nodeAry[startNode];
		if(prefixString.compareTo(cur.key)<=0){
			prefixSearch(cur.lNo, prefixString,results);
			if(isPrefixString(prefixString.toString(),cur.key.toString())){
				results.put(cur.key, cur.value);
			}
		}else{
			prefixSearch(cur.rNo, prefixString,results);
		}
	}
	
	private boolean isPrefixString(String prefixString, String key) {
		if(key.indexOf(prefixString)==0)return true;
		return false;
	}

	private void collectRange(int startNode, K start, K end,Map<K,V> values){
		if(startNode==-1){
			return;
		}
		TreapNode<K, V> node = this.nodeAry[startNode];
		int cp1 = node.key.compareTo(start);
		int cp2 = node.key.compareTo(end);
		collectRange(node.lNo, start, end, values);
		if(cp1>=0 && cp2<0){
			values.put(node.key, node.value);
		}
		collectRange(node.rNo, start, end, values);
	}
	
	//����
	private int rotateRight(int startNode){
		TreapNode<K,V> cur = this.nodeAry[startNode];
		int leftNo = cur.lNo;
		TreapNode<K,V> left = this.nodeAry[leftNo];
		int left_right = left.rNo;
		int left_right_size  = left.r_size;
		left.rNo = startNode;
		left.r_size += cur.r_size+1;
		cur.lNo  = left_right;
		cur.l_size = left_right_size;
		return leftNo;
	}
	
	//����
	private int rotateLeft(int startNode){
		TreapNode<K,V> cur = this.nodeAry[startNode];
		int rightNo = cur.rNo;
		TreapNode <K,V> right = this.nodeAry[rightNo];
		int right_left = right.lNo;
		int right_left_size = right.l_size;
		right.lNo = startNode;
		right.l_size += cur.l_size+1;
		cur.rNo = right_left;
		cur.r_size = right_left_size;
		return rightNo;
	}
	
	
	private int insert(int startNode,K key,V value){
		if(startNode==-1){
			TreapNode<K,V> newNode = new TreapNode<K,V>();
			newNode.key = key;
			newNode.value = value;
			newNode.fix = (int)(Math.random()*Integer.MAX_VALUE);
			if(size+1>this.nodeAry.length){
				resize();
			}
			this.nodeAry[this.size++] = newNode;
			return this.size-1;
			
		}else{
			TreapNode<K,V> currentNode = this.nodeAry[startNode];
			int cp = currentNode.key.compareTo(key);
			if(cp==0){
				this.nodeAry[startNode].value = value;
			}else if(cp<0){
				currentNode.r_size++;
				currentNode.rNo = insert(currentNode.rNo,key,value);
				if(this.nodeAry[currentNode.rNo].fix < currentNode.fix){
					startNode = rotateLeft(startNode);
				}
			}else if(cp>0){
				currentNode.l_size++;
				currentNode.lNo = insert(currentNode.lNo,key,value);
				if(this.nodeAry[currentNode.lNo].fix < currentNode.fix){
					startNode = rotateRight(startNode);
				}
			}
			
		}
		return startNode;
	}
	
	//��������Ϊԭ��������
	private void resize() {
		this.nodeAry = Arrays.copyOf(this.nodeAry, this.nodeAry.length*2);
	}

	private int find(int startNode,K key){
		if(startNode==-1){
			return -1;
		}else{
			TreapNode<K,V> currentNode = this.nodeAry[startNode];
			int cp = currentNode.key.compareTo(key);
			if(cp==0){
				return startNode;
			}else if(cp<0){
				return find(currentNode.rNo,key);
			}else if(cp>0){
				return  find(currentNode.lNo,key);
			}
		}
		return -1;
	}
	
	private void remove(int startNode,K key){
		
	}

	@Override
	public Map<K, V> kmin(int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<K, V> kmax(int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<K, V> before(K key, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<K, V> after(K key, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<K,V> bulkGet(List<K> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bulkPut(Map<K, V> pairs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<K, V> bulkPrefix(List<String> prefixList, int limit, K startK,
			boolean asc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removePrefix(K prefixString) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Entry<K, V> kth(int k, boolean asc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int rank(K key, boolean asc) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
