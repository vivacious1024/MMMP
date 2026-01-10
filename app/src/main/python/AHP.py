# coding=utf-8
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import openpyxl

# 读取xlsx文件的函数，参数为文件路径
def read_excel_file(file_path):
    # 假设数据从第二行第二列开始
    df = pd.read_excel(file_path, index_col=0)
    return df

# 计算权重的函数
def calculate_weights(matrix):
    # 计算成对比较矩阵的特征值和特征向量
    eigenvalues, eigenvectors = np.linalg.eig(matrix)

    # 提取最大特征值对应的特征向量
    max_index = np.argmax(eigenvalues)
    max_eigenvector = np.abs(eigenvectors[:, max_index]).real

    # 归一化该特征向量以得到权重
    weights = max_eigenvector / sum(max_eigenvector)

    return weights

# 一致性检验的函数
def consistency_check(matrix, weights):
    n = matrix.shape[0]
    # 一致性指标CI计算
    lambda_max = sum(np.matmul(matrix, weights) / (n * weights))
    CI = (lambda_max - n) / (n - 1)
    
    # 随机一致性指标RI（根据矩阵的阶数来确定）
    # Extended RI values for n up to 15
    RI_dict = {
        1: 0, 2: 0, 3: 0.58, 4: 0.90, 5: 1.12, 6: 1.24, 7: 1.32, 8: 1.41, 
        9: 1.45, 10: 1.49, 11: 1.51, 12: 1.54, 13: 1.56, 14: 1.58, 15: 1.59
    }
    
    if n in RI_dict:
        RI = RI_dict[n]
    else:
        # Fallback or approximation for n > 15, or just use the last known value/error
        # For simplicity in this context, we'll use 1.59 or handle it gracefully
        print(f"Warning: Matrix size {n} is larger than supported RI table (max 15). Using RI=1.59")
        RI = 1.59

    CR = CI / RI if RI != 0 else 0
    
    return CI, CR, lambda_max


# 绘图函数，参数为权重和标准名
def plot_weights(weights, criteria):
    plot_filename = "/data/data/com.kk.mmmp/cache/AHP_python.png"        #因为是临时文件，这里将其存在缓存中
    plt.bar(criteria, weights, color='skyblue')
    plt.title('Criteria Weights')
    plt.xlabel('Criteria')
    plt.ylabel('Weights')
    plt.xticks(rotation=45)  # 旋转X轴标签以改善可读性
    plt.tight_layout()  # 调整布局以确保所有标签都被包含
    plt.savefig(plot_filename)  # 保存图表为PNG文件
    plt.close()  # 关闭图像，防止后端显示
    return plot_filename  # 返回图像文件名

# 外部调用的主函数，参数为文件路径
def main(file_path):
    # 读取Excel文件
    df = read_excel_file(file_path)
    # DataFrame转换为Numpy数组
    comparison_matrix = df.values

    # 计算权重
    weights = calculate_weights(comparison_matrix)

    # 进行一致性检验
    CI, CR, lambda_max = consistency_check(comparison_matrix, weights)

    # 判断一致性是否通过
    print(f"最大特征值: {lambda_max:.4f}, 一致性指数CI: {CI:.4f}, 一致性比率CR: {CR:.4f}")
    if CR < 0.1:
        print("一致性通过，可以接受")
        # 绘制条形图来可视化各准则的权重
        criteria = df.index
        plot_filename = plot_weights(weights, criteria)
        return weights.tolist(), CI, CR, plot_filename
    else:
        print("一致性未通过，需要调整比较矩阵")
        return [], CI, CR, None
