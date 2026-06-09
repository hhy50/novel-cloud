#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Shiro到Sa-Token Thymeleaf模板标签批量替换脚本
"""

import os
import re

def replace_tags_in_file(file_path):
    """替换单个HTML文件中的Shiro标签"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # 添加Sa-Token命名空间（如果不存在）
        if 'xmlns:sa=' not in content and ('shiro:' in content):
            # 查找thymeleaf命名空间并添加sa命名空间
            content = re.sub(
                r'(<html[^>]*xmlns:th="http://www.thymeleaf.org")',
                r'\1 xmlns:sa="http://sa-token.dev33.cn"',
                content
            )
        
        # 替换Shiro标签为Sa-Token标签
        content = re.sub(r'shiro:hasPermission="([^"]+)"', r'sa:permission="\1"', content)
        content = re.sub(r'shiro:lacksPermission="([^"]+)"', r'sa:notPermission="\1"', content)
        content = re.sub(r'shiro:hasRole="([^"]+)"', r'sa:role="\1"', content)
        content = re.sub(r'shiro:lacksRole="([^"]+)"', r'sa:notRole="\1"', content)
        content = re.sub(r'shiro:hasAnyRoles="([^"]+)"', r'sa:role="\1"', content)
        
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
        return False
        
    except Exception as e:
        print(f'[ERROR] Failed {file_path}: {str(e)}')
        return False

def process_directory(directory):
    """递归处理目录下所有HTML文件"""
    count = 0
    processed_files = []
    
    if not os.path.exists(directory):
        return count, processed_files
    
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.html'):
                file_path = os.path.join(root, file)
                if replace_tags_in_file(file_path):
                    count += 1
                    processed_files.append(file_path)
                    print(f'[OK] Processed: {file_path}')
    
    return count, processed_files

if __name__ == '__main__':
    print('=' * 70)
    print('Shiro to Sa-Token Thymeleaf Template Tag Replacement Tool')
    print('=' * 70)
    print()
    
    # 模板目录列表
    template_dirs = [
        'ruoyi-admin/src/main/resources/templates',
        'ruoyi-quartz/src/main/resources/templates',
        'ruoyi-generator/src/main/resources/templates'
    ]
    
    total_count = 0
    all_processed_files = []
    
    for template_dir in template_dirs:
        if os.path.exists(template_dir):
            print(f'Processing directory: {template_dir}')
            count, files = process_directory(template_dir)
            total_count += count
            all_processed_files.extend(files)
            print(f'Files processed in this directory: {count}')
            print()
    
    print('=' * 70)
    print(f'Total files processed: {total_count}')
    print('=' * 70)
    
    if total_count > 0:
        print()
        print('Summary of changes:')
        print('- shiro:hasPermission    --> sa:permission')
        print('- shiro:lacksPermission  --> sa:notPermission')
        print('- shiro:hasRole          --> sa:role')
        print('- shiro:lacksRole        --> sa:notRole')
        print('- Added xmlns:sa namespace to HTML files')
        print()
        print('Template tag replacement completed!')
