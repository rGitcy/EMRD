import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;


/**
 * sparse matrix 's compute method
 * @author guoyilin1987@gmail.com
 *
 */
public class SparseMatrixUtil {
	public static HashMap<Integer, HashMap<Integer, Double>> multiplyMatrix(HashMap<Integer, HashMap<Integer, Double>> matrixA, HashMap<Integer, HashMap<Integer, Double>> matrixB)
	{
		HashMap<Integer, HashMap<Integer, Double>> result = new HashMap<Integer, HashMap<Integer, Double>>();
		HashMap<Integer, HashMap<Integer, Double>> inverseMatrix = getMatrixTranspose(matrixB);
		Iterator<Integer> it_matrixA = matrixA.keySet().iterator();
		//loop all row
		//int count = 0;
		while (it_matrixA.hasNext()) {
//			count++;
//			if(count%100 == 0)
//				System.out.println(count);
			int row = it_matrixA.next();
			//loop all inverseMatrix's row
			Iterator<Integer> it_inverse = inverseMatrix.keySet().iterator();
			while(it_inverse.hasNext()){
				int row_inverse = it_inverse.next();
				Iterator<Integer> it = inverseMatrix.get(row_inverse).keySet().iterator();
				double rowSum = 0;
				while(it.hasNext())
				{
					int col = it.next();
					if(matrixA.get(row).containsKey(col)){
						rowSum += matrixA.get(row).get(col) * inverseMatrix.get(row_inverse).get(col);
					}
				}
				if(rowSum !=0 )
				{
					if(result.containsKey(row))
					{
						result.get(row).put(row_inverse, rowSum);
					}else{
						HashMap<Integer, Double> map = new HashMap<Integer, Double>();
						map.put(row_inverse, rowSum);
						result.put(row, map);
					}
				}
				
			}
				
		}
		System.gc();
		return result;
	}
	public static HashMap<Integer, HashMap<Integer, Double>> getMatrixTranspose(
			HashMap<Integer, HashMap<Integer, Double>> matrix) {
		HashMap<Integer, HashMap<Integer, Double>> inverseMatrix = new HashMap<Integer, HashMap<Integer, Double>>();
		Iterator<Integer> it = matrix.keySet().iterator();
		while (it.hasNext()) {
			int key = it.next();
			HashMap<Integer, Double> map = matrix.get(key);
			Iterator<Integer> it_map = map.keySet().iterator();
			while (it_map.hasNext()) {
				int key_map = it_map.next();
				double value = map.get(key_map);
				if (inverseMatrix.containsKey(key_map)) {
					inverseMatrix.get(key_map).put(key, value);
				} else {
					HashMap<Integer, Double> temp = new HashMap<Integer, Double>();
					temp.put(key, value);
					inverseMatrix.put(key_map, temp);
				}
			}
		}
		return inverseMatrix;
	}
	public static HashMap<Integer, Double> computeMatrixEigenVector(HashMap<Integer, HashMap<Integer, Double>> matrix, int maxIterator)
	{
		HashMap<Integer, Double> result = new HashMap<Integer, Double>();
		//init
		Iterator<Integer> it = matrix.keySet().iterator();
		while(it.hasNext())
		{	
			int key = it.next();
			result.put(key, 1.0);
		}
		
		for(int i = 0 ; i < maxIterator; i++)
		{
			double diff = 0;
			// calculate the matrix-by-vector product Ab
			HashMap<Integer, Double> newResult = new HashMap<Integer, Double>();
			Iterator<Integer> it2 = matrix.keySet().iterator();
			while(it2.hasNext())
			{
				double rowSum = 0;
				int key2 = it2.next();
				HashMap<Integer, Double> map = matrix.get(key2);
				Iterator<Integer> it3 = map.keySet().iterator();
				while(it3.hasNext())
				{
					int key3 = it3.next();
					rowSum += map.get(key3).doubleValue()*result.get(key3);
				}
				newResult.put(key2, rowSum);
			}
			// calculate the length of the resultant vector
			double norm=0;
			Iterator<Integer> it4 = newResult.keySet().iterator();
			while(it4.hasNext())
			{
				norm += newResult.get(it4.next());
			}
			Iterator<Integer> it5 = newResult.keySet().iterator();
			while(it5.hasNext())
			{
				int it5_key = it5.next();
				newResult.put(it5_key,  newResult.get(it5_key)/norm);
			}		
			//diff
			Iterator<Integer> it6 = result.keySet().iterator();
			while(it6.hasNext())
			{
				int key6 = it6.next();
				diff += absDiff(result.get(key6), newResult.get(key6));
			}
		
			System.out.println("diff:"+diff);
			if(diff < 0.01)
			{
				System.out.println("computeMatrixEigenVector method finished: diff="+diff+ "after iterator :" + i);
				return newResult;
			}else
			{
				result.clear();
				result = newResult;
			}
		}
		System.gc();
		return result;
	}
	public static double absDiff(double a1, double a2)
	{
		if(a1 > a2)
			return a1-a2;
		else
			return a2-a1;
	}
	public static HashMap<Integer, Double> matrixAdd(HashMap<Integer, Double> matrixA, HashMap<Integer, Double> matrixB)
	{
		Iterator<Integer> it = matrixA.keySet().iterator();
		while(it.hasNext())
		{
			int key = it.next();
			double value = matrixA.get(key);
			
			if(matrixB.containsKey(key)){
				double valueB = matrixB.get(key);
				value  = value + valueB;			
			}
			matrixA.put(key, value);
		}
		
		Iterator<Integer> it2 = matrixB.keySet().iterator();
		while(it2.hasNext()){
			int key = it2.next();
			if(!matrixA.containsKey(key)){
				matrixA.put(key, matrixB.get(key));
			}
		}
		return matrixA;
	}
	public static HashMap<Integer, Double> matrixMultiplyNumber(HashMap<Integer, Double> matrix, double number)
	{
		HashMap<Integer, Double> result = new HashMap<Integer, Double>();
		Iterator<Integer> it = matrix.keySet().iterator();
		while(it.hasNext())
		{
			int key = it.next();
			double value = matrix.get(key);
			value *= number;
			result.put(key, value);
		}
		
		return result;
	}
}
