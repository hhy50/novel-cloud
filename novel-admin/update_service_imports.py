#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
更新Controller中的Service导入路径
从 framework.shiro.service 改为 framework.satoken.service
"""

import os
import re

# 配置
BASE_DIR = "."
FILE_PATTERN = "*.java"

# 导入替换映射
IMPORT_REPLACEMENTS = [
    (r'import com\.ruoyi\.framework\.shiro\.service\.SysPasswordService;',
     'import com.ruoyi.framework.satoken.service.SysPasswordService;'),
    (r'import com\.ruoyi\.framework\.shiro\.service\.SysRegisterService;',
     'import com.ruoyi.framework.satoken.service.SysRegisterService;'),
    (r'import com\.ruoyi\.framework\.shiro\.service\.SysLoginService;',
     'import com.ruoyi.framework.satoken.service.SysLoginService;'),
    (r'import com\.ruoyi\.framework\.shiro\.util\.AuthorizationUtils;',
     'import com.ruoyi.framework.satoken.util.AuthorizationUtils;'),
]

def update_file(filepath):
    """更新单个文件"""
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        modified = False
        
        # 应用所有替换规则
        for pattern, replacement in IMPORT_REPLACEMENTS:
            if re.search(pattern, content):
                content = re.sub(pattern, replacement, content)
                modified = True
        
        # 如果有修改，写回文件
        if modified:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(content)
            return True, "Updated"
        else:
            return False, "No changes"
            
    except Exception as e:
        return False, f"Error: {str(e)}"

def find_and_update_files(directory):
    """递归查找并更新所有Java文件"""
    updated_files = []
    error_files = []
    
    for root, dirs, files in os.walk(directory):
        # 跳过target目录和.git目录
        dirs[:] = [d for d in dirs if d not in ['target', '.git', 'node_modules']]
        
        for filename in files:
            if filename.endswith('.java'):
                filepath = os.path.join(root, filename)
                success, message = update_file(filepath)
                
                if success:
                    updated_files.append(filepath)
                    print(f"[OK] {filepath}")
                elif "Error" in message:
                    error_files.append((filepath, message))
                    print(f"[ERROR] {filepath}: {message}")
    
    return updated_files, error_files

def main():
    print("=" * 60)
    print("更新Service导入路径: shiro -> satoken")
    print("=" * 60)
    
    updated, errors = find_and_update_files(BASE_DIR)
    
    print("\n" + "=" * 60)
    print(f"总计更新: {len(updated)} 个文件")
    print(f"错误: {len(errors)} 个文件")
    print("=" * 60)
    
    if updated:
        print("\n已更新的文件:")
        for f in updated:
            print(f"  - {f}")
    
    if errors:
        print("\n错误文件:")
        for f, msg in errors:
            print(f"  - {f}: {msg}")

if __name__ == "__main__":
    main()
