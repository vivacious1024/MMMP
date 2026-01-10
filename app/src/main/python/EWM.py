# coding = utf-8
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

def read_excel_file(file_path):
    df = pd.read_excel(file_path)
    return df

def normalize_data(data):
    normalized_data = (data - np.min(data, axis=0)) / (np.max(data, axis=0) - np.min(data, axis=0))
    return normalized_data

def calculate_proportions(normalized_data):
    p = np.where(normalized_data == 0, 1e-12, normalized_data)
    p = p / p.sum(axis=0)
    return p

def calculate_entropy(p, k):
    e = -k * (p * np.log(p)).sum(axis=0)
    return e

def calculate_entropy_weights(e, num_indices):
    w = (1 - e) / (num_indices - e.sum())
    return w

def normalize_weights(w):
    w_normalized = w / w.sum()
    return w_normalized

def plot_weights(evaluation_index, w_normalized):
    plot_filename = "/data/data/com.kk.mmmp/cache/EWM_python.png"        #因为是临时文件，这里将其存在缓存中
    plt.figure(figsize=(8, 6))
    plt.bar(evaluation_index, w_normalized, color=plt.cm.plasma(np.linspace(0, 1, len(w_normalized))))
    plt.title('Weights of Evaluation Indices')
    plt.xlabel('Indices')
    plt.xticks(rotation=45)
    plt.ylabel('Weight Value')
    plt.tight_layout()
    plt.savefig(plot_filename) # 保存图像
    plt.close()
    return plot_filename

def main(file_path):
    df = read_excel_file(file_path)
    evaluation_index = df.columns[1:]
    data = df[evaluation_index].values
    normalized_data = normalize_data(data)
    p = calculate_proportions(normalized_data)
    k = 1.0 / np.log(normalized_data.shape[0])
    e = calculate_entropy(p, k)
    w = calculate_entropy_weights(e, len(evaluation_index))
    w_normalized = normalize_weights(w)
    plot_filename = plot_weights(evaluation_index, w_normalized)
    return w_normalized, plot_filename

# 调用主函数
# main('path_to_your_file.xlsx')
