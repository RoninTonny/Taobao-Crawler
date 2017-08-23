

    public class BloomFilter{  
        
        // BitMap �Ĵ�С���������Java�Դ���BitSet������boolean��hotspot���������ռ��һ���ֽڵ�ԭ���޷����úܴ�  
        private static final int DEFAULT_SIZE = 1<<28;   
          
        // ��ͬ��ϣ���������ӣ�һ��Ӧȡ����   
        private static final int[] seeds = new int[] {   
            2, 5, 7, 11, 13, 23, 31, 37, 41, 47, 61, 71, 89};  
          
        private BitMap bits = new BitMap(DEFAULT_SIZE);  
          
        // ��ϣ��������   
        private SimpleHash[] func =new SimpleHash[seeds.length];  
          
        public BloomFilter(){  
            for (int i =0; i < seeds.length; i++){  
                func[i] =new SimpleHash(DEFAULT_SIZE, seeds[i]);  
            }  
        }  
      
        /** 
         * ���ַ�����ǵ�bits�� 
         * @param value 
         */  
        public synchronized void add(String value){  
            for (SimpleHash f : func){  
                bits.set(f.hash(value));  
            }  
        }  
      
        /** 
         * �ж��ַ����Ƿ��Ѿ���bits��� 
         * @param value 
         * @return 
         */  
        public synchronized boolean contains(String value){  
            if (value == null){  
                return false;  
            }  
            boolean ret =true;  
            for (SimpleHash f : func){  
                ret = ret && bits.get(f.hash(value))==0?false:true; 
            }  
            return ret;  
        }  
      
        /** 
         * ��ϣ������  
         * @author admin 
         */  
        public static class SimpleHash {  
            private int cap;  
            private int seed;  
      
            public SimpleHash(int cap, int seed){  
                this.cap = cap;  
                this.seed = seed;  
            }  
      
            /** 
             * hash���������ü򵥵ļ�Ȩ��hash 
             * @param value 
             * @return 
             */  
            public int hash(String value){  
                int result =0;  
                int len = value.length();  
                for (int i =0; i < len; i++){  
                    result = seed * result + value.charAt(i);  
                }  
                return (cap -1) & result;  
            }  
        }  
          
        public static class BitMap{  
            private final int INT_BITS = 32;  
            private final int SHIFT = 5 ;// 2^5=32   
            private final int MASK = 0x1f; // 2^5=32  
              
            int bitmap[];  
              
            public BitMap(int size){  
                bitmap = new int[size/INT_BITS];  
            }  
              
            /** 
             * ���õ�iλ 
             * i >> SHIFT �൱�� i / (2 ^ SHIFT), 
             * i&MASK�൱��mod���� m mod n ���� 
             * @param i 
             */  
            void set(int i) {  
                bitmap[i >> SHIFT] |= 1 << (i & MASK);  
            }  
              
            /** 
             * ��ȡ��iλ 
             * @param i 
             * @return 
             */  
            int get(int i) {  
                return bitmap[i >> SHIFT] & (1 << (i & MASK));  
            }  
              
            /** 
             * �����iλ 
             * @param i 
             * @return 
             */  
            int clear(int i) {  
                return bitmap[i >> SHIFT] & ~(1 << (i & MASK));  
            }  
        }  
    }