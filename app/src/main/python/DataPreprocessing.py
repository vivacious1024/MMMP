# coding=utf-8
import pandas as pd
import numpy as np
import os

def handle_missing_values(file_path):
    df = pd.read_excel(file_path)
    # 记录处理前的缺失值数量
    missing_count = df.isnull().sum().sum()
    
    # 使用均值填充数值型列
    numeric_cols = df.select_dtypes(include=[np.number]).columns
    df[numeric_cols] = df[numeric_cols].fillna(df[numeric_cols].mean())
    
    # 保存处理后的文件
    output_path = os.path.join(os.path.dirname(file_path), "preprocessed_data.xlsx")
    df.to_excel(output_path, index=False)
    
    return f"处理完成。共填充了 {missing_count} 个缺失值。", output_path

def handle_outliers(file_path):
    df = pd.read_excel(file_path)
    numeric_cols = df.select_dtypes(include=[np.number]).columns
    outlier_info = ""
    
    for col in numeric_cols:
        mean = df[col].mean()
        std = df[col].std()
        # 3σ原则
        outliers = (df[col] < mean - 3*std) | (df[col] > mean + 3*std)
        count = outliers.sum()
        if count > 0:
            # 将异常值替换为中位数
            df.loc[outliers, col] = df[col].median()
            outlier_info += f"列 '{col}' 处理了 {count} 个异常值；"
            
    if not outlier_info:
        outlier_info = "未检测到明显异常值。"
        
    output_path = os.path.join(os.path.dirname(file_path), "preprocessed_data.xlsx")
    df.to_excel(output_path, index=False)
    
    return outlier_info, output_path

def main(file_path, mode):
    if mode == "缺失值处理":
        return handle_missing_values(file_path)
    elif mode == "异常值处理":
        return handle_outliers(file_path)
    return "未知预处理模式", None
